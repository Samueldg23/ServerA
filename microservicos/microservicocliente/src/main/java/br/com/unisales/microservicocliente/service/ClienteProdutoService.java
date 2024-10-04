package br.com.unisales.microservicocliente.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.unisales.microservicocliente.model.UsuarioDTO;
import br.com.unisales.microservicocliente.repository.ClienteProdutoRepository;
import br.com.unisales.microservicocliente.table.ClienteProduto;

@Service
public class ClienteProdutoService {
    @Autowired
    private ClienteProdutoRepository repo;

    @Autowired
    private RestTemplate rest;
    private final String loginServiceUrl = "http://localhost:8080";

    public ClienteProduto cadastrarProdutoCliente(Integer idUsuario, Integer clienteId, Integer produtoId, Date dataAtivacao, Double precoProduto, Double desconto) {
        verificarAdministrador(idUsuario);

        if (clienteId == null || produtoId == null) {
            throw new IllegalArgumentException("Cliente ou Produto inválido");
        }

        ClienteProduto cadastro = new ClienteProduto();
        cadastro.setClienteId(clienteId);
        cadastro.setProdutoId(produtoId);
        cadastro.setDataAtivacao(dataAtivacao);
        cadastro.setPrecoProduto(precoProduto);

        if (desconto != null && desconto > 0 && desconto <= 100) {
            cadastro.setDesconto(desconto);
        } else {
            cadastro.setDesconto(0.0);
        }

        cadastro.setAtivo(1);
        return repo.save(cadastro);
    }

    public void desativarProdutoCliente(Integer idUsuario, Integer clienteId, Integer produtoId) {
        verificarAdministrador(idUsuario);

        List<ClienteProduto> produtos = repo.findByClienteId(clienteId);
        for (ClienteProduto produto : produtos) {
            if (produto.getProdutoId().equals(produtoId)) {
                produto.setAtivo(0);
                produto.setDataInativacao(new Date());
                repo.save(produto);
            }
        }
    }

    public List<ClienteProduto> listarProdutosCliente(Integer clienteId) {
        return repo.findByClienteId(clienteId);
    }

    private void verificarAdministrador(Integer idUsuario) {
        UsuarioDTO usuario = rest.getForObject(loginServiceUrl + "/buscarUsuario/" + idUsuario, UsuarioDTO.class);

        if (usuario == null || !usuario.getGrupo().equalsIgnoreCase("Administrador")) {
            throw new SecurityException("Acesso negado. Somente administradores podem realizar esta operação.");
        }
    }
}

