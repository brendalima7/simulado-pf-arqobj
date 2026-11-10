package simulado.pf.arqobjet.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import simulado.pf.arqobjet.common.RegraNegocioException;

import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public User create(User user) {
        if (usuarioRepository.existsByEmail(user.getEmail())) {
            throw new RegraNegocioException("Já existe um usuário com este email");
        }

        user.setSenha(passwordEncoder.encode(user.getSenha()));

        return usuarioRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> usuario = usuarioRepository.findByEmail(username);

        if (usuario.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        User user = usuario.get();

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getSenha())
                .build();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            UserDetails userDetails = this.loadUserByUsername(loginRequest.getEmail());

            if (!passwordEncoder.matches(loginRequest.getSenha(), userDetails.getPassword())) {
                throw new BadCredentialsException("Email ou senha inválidos");
            }

            String token = jwtService.generateToken(loginRequest.getEmail());
            return new LoginResponse(token);
        } catch (UsernameNotFoundException e) {
            // mesma mensagem para não revelar se o email existe
            throw new BadCredentialsException("Email ou senha inválidos");
        }
    }
}
