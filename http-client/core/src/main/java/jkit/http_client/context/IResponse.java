package jkit.http_client.context;

public interface IResponse {

    CheckCode response200 = (code) -> code > 200 && code < 300;

    interface CheckCode {
        Boolean checkCode(Integer code);
    }

}
