/**
 * Copyright (c) Fraunhofer IML
 */
package common.telegrams;

/**
 * Declares methods for comm adapters capable of sending telegrams/requests.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public interface TelegramSender {

  /**
   * Sends the given {@link Request}.
   *
   * @param request The {@link Request} to be sent.
   */
  Response sendTelegram(Request request);

  /**
   *响应报文处理。
   * @author Rico
   * @date 2020/3/30 11:42 下午
   */
  void onIncomingTelegram(Response response);
}
