package rest;

import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkiverse.renarde.Controller;
import model.Empresa;
import model.Veiculo;
import repository.EmpresaRepository;
import service.VeiculoService;

public class Veiculos extends Controller {

    @Inject
    VeiculoService veiculoService;

    @Inject
    EmpresaRepository empresaRepository;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance lista(List<Veiculo> veiculos);
        public static native TemplateInstance novo(List<Empresa> empresas);
        public static native TemplateInstance editar(Veiculo veiculo, List<Empresa> empresas);
    }

    public TemplateInstance lista() {
        return Templates.lista(veiculoService.listarAtivos());
    }

    public TemplateInstance novo() {
        return Templates.novo(empresaRepository.listarAtivas());
    }

    @POST
    public void salvar(
            @RestForm @NotNull UUID empresaId,
            @RestForm @NotBlank String placa,
            @RestForm @NotBlank String renavam,
            @RestForm @NotBlank String chassi,
            @RestForm String descricao,
            @RestForm Short anoFabricacao,
            @RestForm Short anoModelo,
            @RestForm String cor,
            @RestForm String combustivel,
            @RestForm String proprietario,
            @RestForm String cpfCnpj,
            @RestForm String situacao) {
        if (validationFailed()) {
            novo();
        }
        veiculoService.criar(empresaId, placa, renavam, chassi, descricao, anoFabricacao, anoModelo,
                cor, combustivel, proprietario, cpfCnpj, situacao);
        lista();
    }

    public TemplateInstance editar(@RestPath UUID id) {
        Veiculo v = veiculoService.buscarPorId(id);
        notFoundIfNull(v);
        return Templates.editar(v, empresaRepository.listarAtivas());
    }

    @POST
    public void atualizar(
            @RestPath UUID id,
            @RestForm @NotNull UUID empresaId,
            @RestForm @NotBlank String placa,
            @RestForm @NotBlank String renavam,
            @RestForm @NotBlank String chassi,
            @RestForm String descricao,
            @RestForm Short anoFabricacao,
            @RestForm Short anoModelo,
            @RestForm String cor,
            @RestForm String combustivel,
            @RestForm String proprietario,
            @RestForm String cpfCnpj,
            @RestForm String situacao) {
        notFoundIfNull(veiculoService.buscarPorId(id));
        if (validationFailed()) {
            editar(id);
        }
        veiculoService.atualizar(id, empresaId, placa, renavam, chassi, descricao, anoFabricacao, anoModelo,
                cor, combustivel, proprietario, cpfCnpj, situacao);
        lista();
    }

    @POST
    public void excluir(@RestPath UUID id) {
        notFoundIfNull(veiculoService.buscarPorId(id));
        veiculoService.excluir(id);
        lista();
    }
}
