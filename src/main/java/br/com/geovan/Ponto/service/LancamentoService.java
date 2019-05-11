package br.com.geovan.Ponto.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.geovan.Ponto.model.Lancamento;
import br.com.geovan.Ponto.repository.LancamentoRepository;
import br.com.geovan.Ponto.to.ResultBaseFactoryTO;

@Service
public class LancamentoService 
{
	@Autowired
	LancamentoRepository repository;

	public ResultBaseFactoryTO inserir(LocalDateTime dataHora)
	{
		ResultBaseFactoryTO response = new ResultBaseFactoryTO();
		if (dataHora != null)
		{
			System.out.println("inserindo lancamento...");
			System.out.println(dataHora);			
			Lancamento saved = repository.save(new Lancamento(dataHora));
			if (saved != null)
				response.setSuccess(new HashMap<String, Object>());
		}
		else
			response.addErrorMessage("Data inv�lida", "Data inv�lida");
		return response;
	}
	
	/***
	 * 
	 * @return
	 */
	public ResultBaseFactoryTO listar()
	{
		ResultBaseFactoryTO response = new ResultBaseFactoryTO();
		Iterable<Lancamento> todosLancamentos = repository.findAll();
		
		if (todosLancamentos != null)
		{
			List<LocalDateTime> lancamentos = new ArrayList<LocalDateTime>();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("lancamentos", todosLancamentos);
			response.setSuccess(map);	
		}
		else
			response.addErrorMessage("n�o foi poss�vel obter a lista de lancamentos", "n�o foi poss�vel obter a lista de lancamentos");
		return response;
	}
	
	/***
	 * 
	 * @param dataHoraLancamentoAntiga
	 * @param dataHoraLancamentoNova
	 * @return
	 */
	public ResultBaseFactoryTO atualizar(LocalDateTime dataHoraLancamentoAntiga, LocalDateTime dataHoraLancamentoNova)
	{
		ResultBaseFactoryTO response = new ResultBaseFactoryTO();
		
		if (dataHoraLancamentoAntiga != null && dataHoraLancamentoNova != null)
		{
			Lancamento lancamentoByDataHoraLancamento = repository.getLancamentoByDataHoraLancamento(dataHoraLancamentoAntiga);
			if (lancamentoByDataHoraLancamento != null)
			{
				lancamentoByDataHoraLancamento.setDataHoraLancamento(dataHoraLancamentoNova);
				Lancamento updated = repository.save(lancamentoByDataHoraLancamento);
				if (updated != null)
				{
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("updated", updated);
					response.setSuccess(map);				
				}
				else
					response.addErrorMessage("n�o foi poss�vel atualizar o lancamento", "n�o foi poss�vel atualizar o lancamento");
			}
			else
				response.addErrorMessage("n�o existe um lancamento com a data/hora informada", "n�o existe um lancamento com a data/hora informada");
		}
		else
			response.addErrorMessage("parametros inv�lidos", "parametros inv�lidos");
		
		return response;
	}
	
	/**
	 * @param dataHoraLancamento
	 * @return
	 */
	public ResultBaseFactoryTO delete(LocalDateTime dataHoraLancamento)
	{
		ResultBaseFactoryTO response = new ResultBaseFactoryTO();
		
		Lancamento lancamentoByDataHoraLancamento = repository.getLancamentoByDataHoraLancamento(dataHoraLancamento);
		
		if (lancamentoByDataHoraLancamento != null)
		{
			repository.delete(lancamentoByDataHoraLancamento);
			response.setSuccess(new HashMap<String, Object>());
		}
		else
			response.addErrorMessage("lancamento n�o encontrado", "lancamento n�o encontrado");
		
		return response;
	}
	
}
