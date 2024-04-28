package pl.dudios.debtor.security.model;

public record AuthRequest(
        String username,
        String password
) {
}
