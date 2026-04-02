ider# 🔧 Solução: Porta 8080 já está em uso

## 🐛 Problema
```
Web server failed to start. Port 8080 was already in use.
```

A porta 8080 já está ocupada por outro processo.

## ✅ Soluções

### Opção 1: Matar o processo que está usando a porta (RECOMENDADO)

#### No PowerShell (como Administrador):
```powershell
# 1. Encontrar o processo usando a porta 8080
netstat -ano | Select-String "8080"

# Você verá algo como:
# TCP    [::]:8080              [::]:0                 LISTENING       19028

# 2. Matar o processo (19028 é o PID encontrado acima)
taskkill /PID 19028 /F
```

#### No Command Prompt (como Administrador):
```cmd
# 1. Encontrar o processo
netstat -ano | findstr "8080"

# 2. Matar o processo
taskkill /PID 19028 /F
```

#### No Linux/Mac:
```bash
# 1. Encontrar o processo
lsof -i :8080

# 2. Matar o processo
kill -9 <PID>
```

### Opção 2: Usar uma porta diferente

Se não conseguir matar o processo, configure a aplicação para usar outra porta:

#### No arquivo `application.yaml`:
```yaml
server:
  port: 8081  # ou qualquer porta disponível
```

Depois execute:
```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=local"
```

E acesse em: `http://localhost:8081`

### Opção 3: Executar com variável de ambiente

```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=local" -Dspring-boot.run.arguments="--server.port=8081"
```

## 📝 Passos Recomendados

1. **Abra PowerShell como Administrador**
2. **Execute**: `netstat -ano | Select-String "8080"`
3. **Copie o PID** (número da última coluna)
4. **Execute**: `taskkill /PID <PID> /F`
5. **Tente novamente**: `mvn spring-boot:run "-Dspring-boot.run.profiles=local"`

Se não conseguir, use a **Opção 2** (mudar a porta).

## ✨ Depois de resolver

Teste a aplicação:
```bash
curl http://localhost:8080/actuator/health
```

Acesse o Swagger:
```
http://localhost:8080/swagger-ui.html
```

