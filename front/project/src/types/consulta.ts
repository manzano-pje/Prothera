import type { Funcionario, FuncionarioIdade, FuncionariosPorFuncao, QuantidadeSalario } from './funcionario'

export type ConsultaResultadoTipo =
  | 'tabela'
  | 'grupo'
  | 'idade'
  | 'numero'
  | 'salarios-minimos'

export interface ConsultaState {
  loading: boolean
  error: string | null
  tipo: ConsultaResultadoTipo | null
  dados: Funcionario[] | FuncionariosPorFuncao | FuncionarioIdade | number | QuantidadeSalario[] | null
}
