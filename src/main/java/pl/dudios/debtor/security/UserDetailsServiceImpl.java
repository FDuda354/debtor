package pl.dudios.debtor.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.dudios.debtor.customer.repository.CustomerDao;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    private final CustomerDao customerDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return customerDao.getCustomerByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }
}
