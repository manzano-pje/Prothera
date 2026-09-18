import { api } from './api'
import type {
  Funcionario,
  FuncionarioRequest,
  FuncionarioIdade,
  FuncionariosPorFuncao,
  QuantidadeSalario,
} from '../types/funcionario'

export async function listarTodos(): Promise<Funcionario[]> {
  const { data } = await api.get<Funcionario[]>('/listarTodos')
  return data
}

export async function criarFuncionario(func: FuncionarioRequest): Promise<Funcionario> {
  const { data } = await api.post<Funcionario>('', func)
  return data
}

export async function atualizarSalarios(): Promise<void> {
  await api.patch('/atualizacao')
}

export async function deletarFuncionario(nome: string): Promise<void> {
  await api.delete(`/delete/${encodeURIComponent(nome)}`)
}

export async function listarPorGrupo(): Promise<FuncionariosPorFuncao> {
  const { data } = await api.get<FuncionariosPorFuncao>('/listaPorGrupo')
  return data
}

export async function listarPorAniversario(): Promise<Funcionario[]> {
  const { data } = await api.get<Funcionario[]>('/listarPorAniversario')
  return data
}

export async function listarMaisVelho(): Promise<FuncionarioIdade> {
  const { data } = await api.get<FuncionarioIdade>('/listarMaisVelho')
  return data
}

export async function listarSomaSalarios(): Promise<number> {
  const { data } = await api.get<number>('/listarSomaSalarios')
  return data
}

export async function listarAlfabetico(): Promise<Funcionario[]> {
  const { data } = await api.get<Funcionario[]>('/listarAlfabetico')
  return data
}

export async function quantidadeSalarios(): Promise<QuantidadeSalario[]> {
  const { data } = await api.get<QuantidadeSalario[]>('/quantidadeSalarios')
  return data
}
