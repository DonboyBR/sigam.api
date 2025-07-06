package com.medeiros.sigam_api.service;

import com.medeiros.sigam_api.model.Ativo;
import com.medeiros.sigam_api.repository.AtivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service

public class AtivoService {
    private final AtivoRepository ativoRepository;

    @Autowired //Injeção de Dependência e construtor
    public AtivoService(AtivoRepository ativoRepository) {
        this.ativoRepository = ativoRepository;
    }

    // Método pra criar e salvar um Ativo
    public Ativo criarAtivo(Ativo ativo) {
        return ativoRepository.save(ativo);
    }

    // Método para buscar todos os Ativos
    public List<Ativo> buscarTodosAtivos() {
        return ativoRepository.findAll();
    }

    // Método para buscar um Ativo por ID
    public Optional<Ativo> buscarAtivoPorId(Long Id) {
        return ativoRepository.findById(Id);
    }

    // Método para atualizar um Ativo existente
    public Ativo atualizarAtivo(Long id, Ativo ativoDetalhes) {
        Optional<Ativo> ativoExistente = ativoRepository.findById(id);

        if (ativoExistente.isPresent()) {
            Ativo ativo = ativoExistente.get();
            // Atualiza os campos que podem ser modificados
            ativo.setNome(ativoDetalhes.getNome());
            ativo.setLocalFisico(ativoDetalhes.getLocalFisico());
            ativo.setDepartamento(ativoDetalhes.getDepartamento());
            ativo.setMarca(ativoDetalhes.getMarca());
            ativo.setModelo(ativoDetalhes.getModelo());
            // Data de registro e ID não são atualizados aqui
            return ativoRepository.save(ativo);
        }
        else { // Caso nao seja encontrado, retorna vazio
            return null;
        }
    }

    // Método para deletar um Ativo por ID
    public boolean deletarAtivo(Long id) {
        if (ativoRepository.existsById(id)) { // Verifica se o ativo existe antes de deletar
            ativoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}