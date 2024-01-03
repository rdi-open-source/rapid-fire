package biz.rapidfire.core.handlers.install;

public interface StatusMessageReceiver {

    public void setStatus(String message);

    public void setErrorStatus(String message);

}
