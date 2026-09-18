export interface Funcionario {
  nome: string
  dataNascimento: string
  salario: number | string
  funcao: string
}

export interface FuncionarioRequest {
  nome: string
  dataNascimento: string
  salario: number
  funcao: string
}

export interface FuncionarioIdade {
  nome: string
  idade: number
}

export interface QuantidadeSalario {
  nome: string
  quantidadeSalario: number
}

export type FuncionariosPorFuncao = Record<string, Funcionario[]>
