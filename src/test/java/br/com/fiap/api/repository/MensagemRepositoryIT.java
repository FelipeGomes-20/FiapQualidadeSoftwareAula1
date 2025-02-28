package br.com.fiap.api.repository;

import br.com.fiap.api.model.Mensagem;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class MensagemRepositoryIT {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Test
    void devePermitirCriarTabela(){
        var totaldeRegistros = mensagemRepository.count();
        assertThat(totaldeRegistros).isGreaterThan(0);
    }

    @Test
    void devePermitirRegistrarMensagem() {
        // Arrange
        var id = UUID.randomUUID();
        var mensagem = gerarMensagem();
        mensagem.setId(id);

        // Act
        var mensagemRecebida = mensagemRepository.save(mensagem);

        // Assert
        assertThat(mensagemRecebida).isInstanceOf(Mensagem.class).isNotNull();
        assertThat(mensagemRecebida.getId()).isEqualTo(id);
        assertThat(mensagemRecebida.getConteudo()).isEqualTo(mensagem.getConteudo());
        assertThat(mensagemRecebida.getUsuario()).isEqualTo(mensagem.getUsuario());
    }

    @Test
    void devePermitirBuscarMensagem(){
        // Arrange
        var id = UUID.fromString("db3693ff-563e-47bc-a4c4-5a667b801d66");

        // Act
        var mensagemRecebidaOptional = mensagemRepository.findById(id);

        // Assert
        assertThat(mensagemRecebidaOptional).isPresent();
        mensagemRecebidaOptional.ifPresent(mensagemRecebida -> {
           assertThat(mensagemRecebida.getId()).isEqualTo(id);
        });
    }

    @Test
    void devePermitirRemoverMensagem(){
        // Arrange
        var id = UUID.fromString("db3693ff-563e-47bc-a4c4-5a667b801d66");
        // Act
        mensagemRepository.deleteById(id);
        // Assert
        var mensagemRemovidaOptional = mensagemRepository.findById(id);
        assertThat(mensagemRemovidaOptional).isEmpty();
    }

    @Test
    void devePermitirListarMensagens(){
        // Act
        var resultadosObtidos = mensagemRepository.findAll();
        // Assert
        assertThat(resultadosObtidos).hasSizeGreaterThan(0);
    }

    private Mensagem gerarMensagem(){
        var mensagem = Mensagem.builder()
                .id(UUID.randomUUID())
                .usuario("José")
                .conteudo("conteúdo da mensagem")
                .build();
        return mensagem;
    }
}
