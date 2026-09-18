<script setup lang="ts">
import type { ConsultaState } from '../types/consulta'
import type { Funcionario, FuncionarioIdade, FuncionariosPorFuncao, QuantidadeSalario } from '../types/funcionario'

defineProps<{
  state: ConsultaState
}>()

function formatarData(valor: string): string {
  if (!valor) return ''
  const partes = valor.includes('T') ? valor.split('T')[0] : valor
  const [ano, mes, dia] = partes.split('-')
  if (!ano || !mes || !dia) return valor
  return `${dia}/${mes}/${ano}`
}

function formatarSalario(valor: number | string): string {
  const n = typeof valor === 'string' ? parseFloat(valor) : valor
  if (isNaN(n)) return String(valor)
  return n.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

function formatarNumero(valor: number): string {
  return valor.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

function isFuncionarios(dados: ConsultaState['dados']): dados is Funcionario[] {
  return Array.isArray(dados) && (dados.length === 0 || 'nome' in dados[0] && 'funcao' in dados[0])
}

function isQuantidadeSalarios(dados: ConsultaState['dados']): dados is QuantidadeSalario[] {
  return Array.isArray(dados) && (dados.length === 0 || 'nome' in dados[0] && 'quantidadeSalario' in dados[0])
}

function isIdade(dados: ConsultaState['dados']): dados is FuncionarioIdade {
  return dados !== null && typeof dados === 'object' && !Array.isArray(dados) && 'idade' in dados
}

function isGrupo(dados: ConsultaState['dados']): dados is FuncionariosPorFuncao {
  return dados !== null && typeof dados === 'object' && !Array.isArray(dados)
}

function isNumero(dados: ConsultaState['dados']): dados is number {
  return typeof dados === 'number'
}
</script>

<template>
  <div class="consulta-resultado">
    <div v-if="state.loading" class="estado estado--loading">
      <span class="spinner"></span> Carregando...
    </div>

    <div v-else-if="state.error" class="estado estado--erro">
      {{ state.error }}
    </div>

    <template v-else-if="state.dados !== null">
      <!-- Tabela de funcionários -->
      <div v-if="state.tipo === 'tabela' && isFuncionarios(state.dados)">
        <p v-if="state.dados.length === 0" class="estado estado--vazio">
          Nenhum funcionário encontrado.
        </p>
        <div v-else class="tabela-wrapper">
          <table class="tabela">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Data de Nascimento</th>
                <th>Salário</th>
                <th>Função</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="f in state.dados" :key="f.nome">
                <td>{{ f.nome }}</td>
                <td>{{ formatarData(f.dataNascimento) }}</td>
                <td>{{ formatarSalario(f.salario) }}</td>
                <td>{{ f.funcao }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Agrupado por função -->
      <div v-else-if="state.tipo === 'grupo' && isGrupo(state.dados)">
        <div v-if="Object.keys(state.dados).length === 0" class="estado estado--vazio">
          Nenhum funcionário encontrado.
        </div>
        <div v-else class="grupos">
          <div v-for="(funcionarios, funcao) in state.dados" :key="funcao" class="grupo">
            <h3 class="grupo__titulo">{{ funcao }}</h3>
            <div class="tabela-wrapper">
              <table class="tabela">
                <thead>
                  <tr>
                    <th>Nome</th>
                    <th>Data de Nascimento</th>
                    <th>Salário</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="f in funcionarios" :key="f.nome">
                    <td>{{ f.nome }}</td>
                    <td>{{ formatarData(f.dataNascimento) }}</td>
                    <td>{{ formatarSalario(f.salario) }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <!-- Funcionário mais velho -->
      <div v-else-if="state.tipo === 'idade' && isIdade(state.dados)" class="resultado-simples">
        <p class="resultado-simples__texto">
          <strong>{{ state.dados.nome }}</strong> — {{ state.dados.idade }} anos
        </p>
      </div>

      <!-- Soma dos salários -->
      <div v-else-if="state.tipo === 'numero' && isNumero(state.dados)" class="resultado-simples">
        <p class="resultado-simples__texto resultado-simples__texto--grande">
          {{ formatarSalario(state.dados) }}
        </p>
      </div>

      <!-- Quantidade de salários mínimos -->
      <div v-else-if="state.tipo === 'salarios-minimos' && isQuantidadeSalarios(state.dados)">
        <p v-if="state.dados.length === 0" class="estado estado--vazio">
          Nenhum funcionário encontrado.
        </p>
        <ul v-else class="lista-salarios">
          <li v-for="item in state.dados" :key="item.nome">
            <strong>{{ item.nome }}</strong> — {{ formatarNumero(item.quantidadeSalario) }} salários mínimos
          </li>
        </ul>
      </div>
    </template>
  </div>
</template>

<style scoped>
.consulta-resultado {
  margin-top: 1rem;
}

.estado {
  padding: 1rem;
  border-radius: 8px;
  font-size: 0.95rem;
}

.estado--loading {
  color: #475569;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.estado--erro {
  background-color: #fef2f2;
  color: #b91c1c;
  border: 1px solid #fecaca;
}

.estado--vazio {
  color: #64748b;
  background-color: #f8fafc;
  border: 1px solid #e2e8f0;
}

.spinner {
  width: 18px;
  height: 18px;
  border: 2px solid #cbd5e1;
  border-top-color: #2563eb;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
  display: inline-block;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.tabela-wrapper {
  overflow-x: auto;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
}

.tabela {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
}

.tabela th,
.tabela td {
  padding: 0.7rem 0.9rem;
  text-align: left;
  border-bottom: 1px solid #e2e8f0;
  white-space: nowrap;
}

.tabela th {
  background-color: #f1f5f9;
  color: #475569;
  font-weight: 600;
  font-size: 0.8rem;
  text-transform: uppercase;
  letter-spacing: 0.03em;
}

.tabela tbody tr:last-child td {
  border-bottom: none;
}

.tabela tbody tr:hover {
  background-color: #f8fafc;
}

.grupos {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.grupo__titulo {
  font-size: 1rem;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 0.5rem;
  padding-bottom: 0.4rem;
  border-bottom: 2px solid #2563eb;
}

.resultado-simples {
  padding: 1.25rem;
  background-color: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
}

.resultado-simples__texto {
  margin: 0;
  font-size: 1.1rem;
  color: #1e293b;
}

.resultado-simples__texto--grande {
  font-size: 1.5rem;
  font-weight: 600;
  color: #2563eb;
}

.lista-salarios {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.lista-salarios li {
  padding: 0.75rem 1rem;
  background-color: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 0.95rem;
  color: #1e293b;
}
</style>
