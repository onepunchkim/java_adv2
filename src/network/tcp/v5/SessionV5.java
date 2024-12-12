package network.tcp.v5;

import java.io.*;
import java.net.Socket;

import static network.tcp.SocketCloseUtil.closeAll;
import static util.MyLogger.log;

public class SessionV5 implements Runnable{
    private final Socket socket;

    public SessionV5(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (socket;
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            while (true) {
                //클라이언트로부터 문자 받기
                String received = input.readUTF(); //블로킹
                log("client -> server: " + received);

                //클라이언트 종료 시 서버도 함께 종료
                if (received.equals("exit")) {
                    break;
                }

                //클라이언트에게 문자 보내기
                String toSend = received + " World!";
                output.writeUTF(toSend);
                log("client <- server: " + toSend);
            }

            //자원 정리
        } catch (IOException e) {
            log(e);
        }
        log("연결 종료: " + socket + "isClosed: " + socket.isClosed());
    }
}
