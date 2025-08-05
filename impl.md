# 🚀 ROADMAP DE IMPLEMENTAÇÃO — SISTEMA DE VOTAÇÃO ONLINE

## ✅ Fase 1 — Finalização do MVP
**Prioridade:** Alta

- [x] Autenticação com JWT (login/registro)
- [x] Criação de votação com opções
- [x] Voto com regra de único voto por usuário
- [x] Visibilidade do resultado (público/privado)
- [x] Exibição dos resultados com contagem e porcentagem
- [x] Endpoint para buscar resultados da votação
  - [x] Listagem de votações públicas (com paginação)

## ✍️ Fase 2 — Usabilidade e controle
**Prioridade:** Alta

- [x] Editar votação (somente se não houver votos)
- [x] Excluir votação
- [x] Encerrar votação manualmente
- [x] Marcar votação como destaque (para mostrar no topo)
  - [x] Filtro de votações por status (ativas, encerradas)

## 👤 Fase 3 — Recursos de usuário
**Prioridade:** Média

- [ ] Histórico de votações criadas
- [ ] Histórico de votações votadas
- [ ] Favoritar votações
- [ ] Ver quem votou (em votações não anônimas)
  - [ ] Sistema de perfis: editar nome, senha, etc.

## 📊 Fase 4 — Resultados e visualização
**Prioridade:** Média a Alta

```json
{
  "totalVotes": 100,
  "options": [
    {
      "text": "Opção A",
      "votes": 60,
      "percentage": 60.0
    }
  ]
}
```

- [ ] Exportar resultado em PDF ou CSV
  - [ ] Dashboard com gráficos (usar API ou frontend futuro)

## 🗳️ Fase 5 — Avanço em votação
**Prioridade:** Alta para diferencial

- [ ] Votação com múltiplas seleções
- [ ] Limite de votos por IP
- [ ] Votação por link único/token
  - [ ] Agendar início/fim da votação

## 📡 Fase 6 — Compartilhamento e engajamento
**Prioridade:** Baixa

- [ ] Link público da votação (com UUID seguro)
- [ ] Compartilhar por QR Code
- [ ] Comentários em votações
  - [ ] Página pública para acompanhamento de resultados

## 🌐 Fase 7 — Técnicas e escalabilidade
**Prioridade:** Média/Alta

- [ ] Internacionalização (pt-BR, en-US)
- [ ] WebSocket para resultados em tempo real

Testes automatizados (unitários e integração)

    Logs e auditoria de ações (voto, exclusão etc.)

🔚 Bônus

Votação com pesos diferentes por usuário

Votação condicional (ex: só após login via Google)

Integração com e-mail para envio de convites