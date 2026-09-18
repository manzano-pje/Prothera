<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { Funcionario, FuncionarioRequest } from '../types/funcionario'
import {
  listarTodos,
  criarFuncionario,
  atualizarSalarios,
  deletarFuncionario,
} from '../services/protheraService'
import ConfirmDialog from '../components/ConfirmDialog.vue'

const funcionarios = ref<Funcionario[]>([])
const carregandoLista = ref(false)
const erroLista = ref<string | null>(null)

const form = ref<FuncionarioRequest>({
  nome: '',
  dataNascimento: '',
  salario: 0,
  funcao: '',
})

const salvando = ref(false)
const atualizando = ref(false)
const mensagem = ref<{ tipo: 'sucesso' | 'erro'; texto: string } | null>(null)

const funcionarioSelecionado = ref<string | null>(null)
const dialogoExclusao = ref(false)
const excluindo = ref(false)

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

async function carregarFuncionarios() {
  carregandoLista.value = true
  erroLista.value = null
  try {
    funcionarios.value = await listarTodos()
  } catch (e) {
    erroLista.value = e instanceof Error ? e.message : 'Não foi possível carregar os funcionários.'
  } finally {
    carregandoLista.value = false
  }
}

function limparForm() {
  form.value = { nome: '', dataNascimento: '', salario: 0, funcao: '' }
  mensagem.value = null
}

async function cadastrar() {
  if (!form.value.nome || !form.value.dataNascimento || !form.value.funcao || !form.value.salario) {
    mensagem.value = { tipo: 'erro', texto: 'Preencha todos os campos.' }
    return
  }
  if (form.value.salario <= 0) {
    mensagem.value = { tipo: 'erro', texto: 'O salário deve ser maior que zero.' }
    return
  }
  salvando.value = true
  mensagem.value = null
  try {
    await criarFuncionario({ ...form.value, salario: Number(form.value.salario) })
    mensagem.value = { tipo: 'sucesso', texto: 'Funcionário cadastrado com sucesso.' }
    limparForm()
    await carregarFuncionarios()
  } catch (e) {
    mensagem.value = { tipo: 'erro', texto: e instanceof Error ? e.message : 'Não foi possível cadastrar o funcionário.' }
  } finally {
    salvando.value = false
  }
}

async function atualizarSalariosAction() {
  atualizando.value = true
  mensagem.value = null
  try {
    await atualizarSalarios()
    mensagem.value = { tipo: 'sucesso', texto: 'Salários atualizados em 10% com sucesso.' }
    await carregarFuncionarios()
  } catch (e) {
    mensagem.value = { tipo: 'erro', texto: e instanceof Error ? e.message : 'Não foi possível atualizar os salários.' }
  } finally {
    atualizando.value = false
  }
}

function selecionarFuncionario(nome: string) {
  funcionarioSelecionado.value = funcionarioSelecionado.value === nome ? null : nome
}

function abrirDialogoExclusao() {
  if (!funcionarioSelecionado.value) {
    mensagem.value = { tipo: 'erro', texto: 'Selecione um funcionário para excluir.' }
    return
  }
  dialogoExclusao.value = true
}

async function confirmarExclusao() {
  if (!funcionarioSelecionado.value) return
  excluindo.value = true
  try {
    await deletarFuncionario(funcionarioSelecionado.value)
    mensagem.value = { tipo: 'sucesso', texto: 'Funcionário excluído com sucesso.' }
    funcionarioSelecionado.value = null
    dialogoExclusao.value = false
    await carregarFuncionarios()
  } catch (e) {
    mensagem.value = { tipo: 'erro', texto: e instanceof Error ? e.message : 'Não foi possível excluir o funcionário.' }
    dialogoExclusao.value = false
  } finally {
    excluindo.value = false
  }
}

onMounted(carregarFuncionarios)
</script>

<template>
  <section class="page">
    <h2 class="page__titulo">Funcionários</h2>

    <transition name="fade">
      <div v-if="mensagem" :class="['feedback', `feedback--${mensagem.tipo}`]">
        {{ mensagem.texto }}
      </div>
    </transition>

    <div class="card">
      <h3 class="card__titulo">Cadastrar funcionário</h3>
      <form class="form" @submit.prevent="cadastrar">
        <div class="form__grid">
          <div class="form__campo">
            <label for="nome">Nome</label>
            <input id="nome" v-model="form.nome" type="text" placeholder="Nome do funcionário" required />
          </div>
          <div class="form__campo">
            <label for="dataNascimento">Data de nascimento</label>
            <input id="dataNascimento" v-model="form.dataNascimento" type="date" required />
          </div>
          <div class="form__campo">
            <label for="salario">Salário</label>
            <input id="salario" v-model.number="form.salario" type="number" step="0.01" min="0.01" placeholder="0,00" required />
          </div>
          <div class="form__campo">
            <label for="funcao">Função</label>
            <input id="funcao" v-model="form.funcao" type="text" placeholder="Função do funcionário" required />
          </div>
        </div>
        <div class="form__acoes">
          <button type="submit" class="btn btn--primary" :disabled="salvando">
            {{ salvando ? 'Cadastrando...' : 'Cadastrar' }}
          </button>
          <button type="button" class="btn btn--secondary" :disabled="salvando" @click="limparForm">
            Limpar
          </button>
          <button type="button" class="btn btn--accent" :disabled="atualizando" @click="atualizarSalariosAction">
            {{ atualizando ? 'Atualizando...' : 'Atualizar salários em 10%' }}
          </button>
          <button type="button" class="btn btn--danger" :disabled="excluindo" @click="abrirDialogoExclusao">
            Excluir
          </button>
        </div>
      </form>
    </div>

    <div class="card">
      <h3 class="card__titulo">Funcionários cadastrados</h3>
      <div v-if="carregandoLista" class="estado estado--loading">
        <span class="spinner"></span> Carregando funcionários...
      </div>
      <div v-else-if="erroLista" class="estado estado--erro">
        {{ erroLista }}
      </div>
      <div v-else-if="funcionarios.length === 0" class="estado estado--vazio">
        Nenhum funcionário cadastrado.
      </div>
      <div v-else class="tabela-wrapper">
        <table class="tabela">
          <thead>
            <tr>
              <th style="width: 2.5rem"></th>
              <th>Nome</th>
              <th>Data de Nascimento</th>
              <th>Salário</th>
              <th>Função</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="f in funcionarios"
              :key="f.nome"
              :class="{ 'linha-selecionada': funcionarioSelecionado === f.nome }"
              @click="selecionarFuncionario(f.nome)"
            >
              <td class="col-check">
                <input type="radio" :checked="funcionarioSelecionado === f.nome" readonly />
              </td>
              <td>{{ f.nome }}</td>
              <td>{{ formatarData(f.dataNascimento) }}</td>
              <td>{{ formatarSalario(f.salario) }}</td>
              <td>{{ f.funcao }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <p v-if="funcionarioSelecionado" class="selecao-info">
        Funcionário selecionado: <strong>{{ funcionarioSelecionado }}</strong>
      </p>
    </div>

    <ConfirmDialog
      :open="dialogoExclusao"
      :message="`Deseja realmente excluir o funcionário ${funcionarioSelecionado || ''}?`"
      confirm-text="Excluir"
      :loading="excluindo"
      @confirm="confirmarExclusao"
      @cancel="dialogoExclusao = false"
    />
  </section>
</template>

<style scoped>
.page__titulo {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 1.5rem;
}

.card {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  padding: 1.5rem;
  margin-bottom: 1.5rem;
}

.card__titulo {
  font-size: 1.1rem;
  font-weight: 600;
  color: #334155;
  margin: 0 0 1.25rem;
}

.form__grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.25rem;
}

.form__acoes {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
  margin-top: 1.5rem;
}

.btn {
  border-radius: 6px;
  padding: 0.6em 1.2em;
  font-size: 0.95rem;
  font-weight: 500;
}

.btn--primary {
  background-color: #2563eb;
  color: #ffffff;
}

.btn--primary:hover:not(:disabled) {
  background-color: #1d4ed8;
}

.btn--secondary {
  background-color: #f1f5f9;
  color: #475569;
  border: 1px solid #cbd5e1;
}

.btn--secondary:hover:not(:disabled) {
  background-color: #e2e8f0;
}

.btn--accent {
  background-color: #059669;
  color: #ffffff;
}

.btn--accent:hover:not(:disabled) {
  background-color: #047857;
}

.btn--danger {
  background-color: #dc2626;
  color: #ffffff;
}

.btn--danger:hover:not(:disabled) {
  background-color: #b91c1c;
}

.feedback {
  padding: 0.85rem 1rem;
  border-radius: 8px;
  font-size: 0.9rem;
  margin-bottom: 1.5rem;
}

.feedback--sucesso {
  background-color: #ecfdf5;
  color: #065f46;
  border: 1px solid #a7f3d0;
}

.feedback--erro {
  background-color: #fef2f2;
  color: #991b1b;
  border: 1px solid #fecaca;
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

.tabela tbody tr {
  cursor: pointer;
  transition: background-color 0.15s;
}

.tabela tbody tr:hover {
  background-color: #f8fafc;
}

.linha-selecionada {
  background-color: #eff6ff !important;
}

.col-check {
  text-align: center;
}

.selecao-info {
  margin-top: 0.75rem;
  font-size: 0.875rem;
  color: #475569;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 640px) {
  .form__grid {
    grid-template-columns: 1fr;
  }
}
</style>
