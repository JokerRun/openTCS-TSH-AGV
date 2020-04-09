/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.common.telegrams;

import com.google.inject.assistedinject.Assisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

import static java.util.Objects.requireNonNull;

/**
 * Keeps {@link Request}s in a queue and matches them with incoming {@link Response}s.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class RequestResponseMatcher {

    /**
     * This class's logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(RequestResponseMatcher.class);
    /**
     * The actual queue of requests.
     */
    private final Queue<Request> requests = new LinkedList<>();
    /**
     * Sends the queued {@link Request}s.
     */
    private final TelegramSender telegramSender;

    /**
     * Creates a new instance.
     *
     * @param telegramSender Sends the queued {@link Request}s.
     */
    @Inject
    public RequestResponseMatcher(@Assisted TelegramSender telegramSender) {
        this.telegramSender = requireNonNull(telegramSender, "telegramSender");
    }

    public void enqueueRequest(@Nonnull Request request) {
        requireNonNull(request, "request");
        boolean emptyQueueBeforeEnqueue = requests.isEmpty();

        LOG.debug("请求队列新增请求:()， 当前待发送请求数为：{}", request.getId(), requests.size());
        requests.add(request);
        // 根据requests数量控制？？？ 此处是否需要控制？
        if (emptyQueueBeforeEnqueue) {
            checkForSendingNextRequest();
        } else {
            checkForSendingNextRequest();
        }
    }

    /**
     * Checks if a telegram is enqueued and sends it.
     */
    public void checkForSendingNextRequest() {
        if (requests.isEmpty()) return;
        //requests会被【小车状态周期查询任务进程】 、 及【小车指令分发进程】共用，估需要在此处做控制。
        //另外，model中小车【小车状态周期查询任务进程】间隔建议大于一个请求+处理的时间周期， 不然容易锁死。。。
        //<property name="example:stateRequestInterval" value="1500"/>
        // TODO. 死锁处理
        LOG.debug("******** {}线程请求requests锁 **********", Thread.currentThread().getName());
//        synchronized (requests) {
            LOG.debug(" ********** {}线程拿到requests锁 ********** ", Thread.currentThread().getName());
            LOG.info("检查是否存在待发送请求.., 当前requests数量为:{}", requests.size());
            Request peek = requests.peek();
            if (!Objects.isNull(peek)) {
                Response response;
                response = telegramSender.sendTelegram(peek);
                if (Objects.isNull(response)) return;
                telegramSender.onIncomingTelegram(response);
                requests.remove(peek);
            } else {
                LOG.debug("未找到待发送请求.");
            }
            LOG.info(" ********** {}线程释放requests锁 ********** ", Thread.currentThread().getName());

//        }
        //Send the next telegram if one is waiting
        checkForSendingNextRequest();
    }

    /**
     * Returns the next request in the queue or an {@link Optional# EMPTY} if none is present.
     *
     * @return The next request in the queue or an {@link Optional# EMPTY} if none is present
     */
    public Optional<Request> peekCurrentRequest() {
        return Optional.ofNullable(requests.peek());
    }

    /**
     * Returns <code>true</code> if the response matches to the first request in the queue.
     * If it matches, the request will be removed.
     *
     * @param response The response to match
     * @return <code>true</code> if the response matches to the first request in the queue.
     */
    public boolean tryMatchWithCurrentRequest(@Nonnull Response response) {
        requireNonNull(response, "response");

        Request currentRequest = requests.peek();
        if (currentRequest != null && response.isResponseTo(currentRequest)) {
            LOG.debug("收到响应：{}, 匹配到请求：{}, 即将从请求队列({})中剔除。", response, currentRequest, requests.size());
            requests.remove();
            return true;
        }

        if (currentRequest != null) {
            LOG.info("No request matching response with counter {}. Latest request counter is {}.",
                    response.getId(), currentRequest.getId());
        } else {
            LOG.info("Received response with counter {}, but no request is waiting for a response.",
                    response.getId());
        }

        return false;
    }

    /**
     * Clears all requests stored in the queue.
     */
    public void clear() {
        LOG.debug("即将清空请求队列requests({})...", requests.size());
        requests.clear();
    }
}
