package com.medeiros.sigam_api.service;

import com.medeiros.sigam_api.model.Ativo;
import com.medeiros.sigam_api.model.Problema;
import com.medeiros.sigam_api.repository.AtivoRepository;
import com.medeiros.sigam_api.repository.ProblemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemaService {

    private final ProblemaRepository problemaRepository;
    private final AtivoRepository ativoRepository;


    @Autowired
    public ProblemaService(ProblemaRepository problemaRepository, AtivoRepository ativoRepository) {
        this.problemaRepository = problemaRepository;
        this.ativoRepository = ativoRepository;
    }


    // Método para criar/salvar um novo Problema
    public Problema criarProblema(Long ativoId, Problema problema) {
        Optional<Ativo> ativoOptional = ativoRepository.findById(ativoId);

        if (ativoOptional.isPresent()) {
            problema.setAtivo(ativoOptional.get()); // Conecta o problema ao ativo encontrado
            problema.setDataRegistro(LocalDateTime.now()); // Garante a data de registro
            problema.setStatus("Pendente"); // Define o status inicial
            return problemaRepository.save(problema); // Salva o problema no banco
        } else {
            throw new RuntimeException("Ativo com ID " + ativoId + " não encontrado.");
        }
    }

    // Método para buscar todos os Problemas
    public List<Problema> buscarTodosProblemas() {
        return problemaRepository.findAll();
    }

    // Método para buscar um Problema por ID
    public Optional<Problema> buscarProblemaPorId(Long id) {
        return problemaRepository.findById(id);
    }

    // Método para atualizar um Problema existente
    public Problema atualizarProblema(Long id, Problema problemaDetalhes) {
        Optional<Problema> problemaExistente = problemaRepository.findById(id);

        if (problemaExistente.isPresent()) {
            Problema problema = problemaExistente.get();
            problema.setDescricao(problemaDetalhes.getDescricao());
            problema.setTipoProblema(problemaDetalhes.getTipoProblema());
            problema.setStatus(problemaDetalhes.getStatus());

            if (problemaDetalhes.getDataResolucao() != null) {
                problema.setDataResolucao(problemaDetalhes.getDataResolucao());
            }
            problema.setObservacoesTecnico(problemaDetalhes.getObservacoesTecnico());

            // O Ativo associado não deve ser alterado por aqui, apenas os detalhes do problema
            return problemaRepository.save(problema);
        } else {
            return null; // Ou lançar uma exceção
        }
    }

    // Método para deletar um Problema por ID
    public boolean deletarProblema(Long id) {
        if (problemaRepository.existsById(id)) {
            problemaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}


