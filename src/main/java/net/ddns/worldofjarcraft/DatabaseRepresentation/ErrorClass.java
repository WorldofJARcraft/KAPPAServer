package net.ddns.worldofjarcraft.DatabaseRepresentation;

public class ErrorClass {
    private String error_message;

    public String getError_message() {
        return error_message;
    }

    public ErrorClass(String error_message) {

        this.error_message = error_message;
    }
}
