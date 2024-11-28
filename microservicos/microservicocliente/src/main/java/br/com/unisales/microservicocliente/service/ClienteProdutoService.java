package br.com.unisales.microservicocliente.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.unisales.microservicocliente.repository.ClienteProdutoRepository;
import br.com.unisales.microservicocliente.table.ClienteProduto;

@Service
public class ClienteProdutoService {
    @Autowired
    private ClienteProdutoRepository repo;

    public ClienteProduto cadastrarProdutoCliente(Integer clienteId, Integer produtoId, Date dataAtivacao) {
        if (clienteId == null || produtoId == null) {
            throw new IllegalArgumentException("Cliente ou Produto inválido");
        }
    
        // verificar se já existe uma associação ativa
        List<ClienteProduto> existentes = repo.findByClienteIdAndProdutoIdAndAtivo(clienteId, produtoId, 1);
        if (!existentes.isEmpty()) {
            throw new IllegalArgumentException("Produto já está associado a este cliente.");
        }
    
        ClienteProduto cadastro = new ClienteProduto();
        cadastro.setClienteId(clienteId);
        cadastro.setProdutoId(produtoId);
        cadastro.setDataAtivacao(dataAtivacao != null ? dataAtivacao : new Date());
        cadastro.setAtivo(1);
    
        return repo.save(cadastro);
    }
    

    public void desativarProdutoCliente(Integer clienteId, Integer produtoId) {
        List<ClienteProduto> produtos = repo.findByClienteIdAndProdutoIdAndAtivo(clienteId, produtoId, 1);
    
        if (produtos.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma associação ativa encontrada para este cliente e produto.");
        }
    
        for (ClienteProduto produto : produtos) {
            produto.setAtivo(0);
            produto.setDataInativacao(new Date());
            repo.save(produto);
        }
    }
    

    public List<ClienteProduto> listarProdutosCliente(Integer clienteId) {
        return repo.findByClienteId(clienteId);
    }

    public List<Integer> obterProdutosPorCliente(Integer clienteId) {
        List<ClienteProduto> registros = repo.findByClienteId(clienteId);
        return registros.stream()
                .map(ClienteProduto::getProdutoId)
                .collect(Collectors.toList());
    }

}
