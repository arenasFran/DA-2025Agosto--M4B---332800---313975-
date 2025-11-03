# 📡 Sistema de Notificaciones en Tiempo Real con SSE + Patrón Observador

## 🎯 ¿Qué se implementó?

Se implementó un sistema completo de **notificaciones en tiempo real** usando:

- **Patrón Observador** (Observer Pattern)
- **SSE** (Server-Sent Events) para comunicación servidor → cliente
- **Scope Session** para mantener conexiones individuales por administrador

## 🏗️ Arquitectura

```
┌──────────────────────────────────────────────────────────────┐
│                    NAVEGADOR (Cliente)                        │
├──────────────────────────────────────────────────────────────┤
│  bonificaciones.html                                          │
│    ↓                                                          │
│  sse.js (EventSource) ←──── SSE Connection ───────────┐      │
│    ↓                                                   │      │
│  procesarMensajeSSE()                                  │      │
│    ↓                                                   │      │
│  procesarResultadosSubmit()                            │      │
│    ↓                                                   │      │
│  window['mostrar_notificacion']()                      │      │
└────────────────────────────────────────────────────────┼──────┘
                                                         │
                                                         │ HTTP SSE
                                                         │
┌────────────────────────────────────────────────────────┼──────┐
│                    SERVIDOR (Spring Boot)              ↓      │
├──────────────────────────────────────────────────────────────┤
│  ControladorAdministrador (scope: session)                    │
│    - implements Observador                                    │
│    - GET /administrador/registrarSSE → SseEmitter            │
│    - POST /administrador/vistaConectada                      │
│    - POST /administrador/vistaCerrada                        │
│    - actualizar(evento, origen) → envía via SSE              │
│         ↑                                    ↓                │
│         │                          ConexionNavegador         │
│         │                          (scope: session)          │
│         │                            - SseEmitter            │
│         │                            - enviarJSON()          │
│    Observable                                                 │
│         ↑                                                     │
│    Fachada extends Observable                                 │
│      - enum Eventos { nuevaAsignacion }                      │
│      - asignarBonificacion() → avisar(nuevaAsignacion)      │
│         ↓                                                     │
│    SistemaAsignacion                                          │
│      - asignarBonificacion()                                  │
└──────────────────────────────────────────────────────────────┘
```

## 📂 Archivos Creados/Modificados

### ✅ Nuevos archivos

1. **`observador/Observable.java`**

   - Clase base del patrón Observer
   - Mantiene lista de observadores
   - Método `avisar(evento)` notifica a todos

2. **`observador/Observador.java`**

   - Interfaz del patrón Observer
   - Método `actualizar(evento, origen)`

3. **`ConexionNavegador.java`**

   - Scope: session (una por sesión HTTP)
   - Maneja la conexión SSE con el navegador
   - Métodos: `conectarSSE()`, `enviarJSON()`, `cerrarConexion()`

4. **`controllers/ControladorAdministrador.java`**

   - Scope: session
   - Implementa `Observador`
   - Endpoints SSE: `/administrador/registrarSSE`
   - Se registra/quita como observador
   - Recibe eventos y los envía via SSE

5. **`static/sse.js`**
   - Cliente JavaScript para SSE
   - Conecta automáticamente al endpoint
   - Procesa mensajes JSON del servidor
   - Auto-reconexión en caso de error

### ✏️ Archivos modificados

6. **`Fachada.java`**

   - Ahora extiende `Observable`
   - Define `enum Eventos { nuevaAsignacion }`
   - `asignarBonificacion()` llama a `avisar(Eventos.nuevaAsignacion)`

7. **`bonificaciones.html`**
   - Incluye `<script src="sse.js"></script>`
   - Define `urlRegistroSSE`, `urlIniciarVista`, `urlCierreVista`
   - Handler `mostrar_notificacion(parametro)` para notificaciones
   - Handler `conexionSSECerrada(event)` para errores de conexión

## 🔄 Flujo de Ejecución

### 1️⃣ **Inicialización (cuando un admin abre la página)**

```
1. Usuario abre bonificaciones.html
2. Se cargan vistaWeb.js y sse.js
3. vistaWeb.js detecta urlIniciarVista definida
4. vistaWeb.js hace POST /administrador/vistaConectada
5. ControladorAdministrador.inicializarVista() se ejecuta:
   - Fachada.getInstancia().agregarObservador(this)
   - Retorna respuesta con mensaje de bienvenida
6. vistaWeb.js llama primerSubmitFinalizado()
7. sse.js.registrarSSE() se ejecuta:
   - Crea EventSource(urlRegistroSSE)
   - GET /administrador/registrarSSE
8. ConexionNavegador.conectarSSE() crea SseEmitter
9. ¡Conexión SSE establecida! 🎉
```

### 2️⃣ **Asignación de bonificación (trigger del evento)**

```
1. Admin 1 asigna una bonificación
2. POST /bonificaciones/asignar
3. BonificacionController.asignarBonificacion()
4. Fachada.asignarBonificacion()
5. SistemaAsignacion.asignarBonificacion()
6. propietario.agregarAsignacion(asignacion)
7. Fachada.avisar(Eventos.nuevaAsignacion) ← ¡AQUÍ!
8. Observable.avisar() itera sobre todos los observadores
```

### 3️⃣ **Notificación (todos los admins conectados)**

```
Para CADA admin conectado:
  1. ControladorAdministrador.actualizar() se llama
  2. Verifica if (evento == Fachada.Eventos.nuevaAsignacion)
  3. conexionNavegador.enviarJSON(respuesta)
  4. SseEmitter.send(json) → Envía al navegador

En el navegador del admin:
  5. EventSource.onmessage recibe el evento
  6. sse.js parsea el JSON
  7. procesarMensajeSSE(json)
  8. procesarResultadosSubmit(json)
  9. window['mostrar_notificacion'](parametro)
  10. mostrarNotificacion() muestra el toast
  11. Si hay un propietario buscado, se actualiza automáticamente
```

### 4️⃣ **Cierre de conexión**

```
1. Usuario cierra la página (o cambia de página)
2. window.beforeunload ejecuta
3. vistaWeb.js hace POST /administrador/vistaCerrada
4. ControladorAdministrador.vistaCerrada():
   - Fachada.quitarObservador(this)
   - conexionNavegador.cerrarConexion()
5. SseEmitter.complete()
6. EventSource se cierra en el navegador
```

## 🔑 Conceptos Clave

### 🎭 **Patrón Observador**

**Problema**: ¿Cómo notificar a múltiples objetos cuando ocurre un evento sin crear dependencias fuertes?

**Solución**:

- **Observable** (Subject): Mantiene lista de observadores y los notifica
- **Observador** (Observer): Interfaz que implementan los objetos que quieren ser notificados
- **Relación**: 1 Observable → N Observadores

**En nuestro caso**:

- **Observable**: `Fachada` (extiende Observable)
- **Observador**: `ControladorAdministrador` (implementa Observador)
- **Evento**: `Fachada.Eventos.nuevaAsignacion`

### 📡 **SSE (Server-Sent Events)**

**¿Qué es?**: Protocolo HTTP para enviar eventos del servidor al cliente de forma unidireccional.

**Ventajas sobre WebSockets**:

- ✅ Más simple (solo servidor → cliente)
- ✅ HTTP estándar (no requiere protocolo especial)
- ✅ Auto-reconexión automática
- ✅ Funciona con proxies/firewalls HTTP

**Características**:

- Content-Type: `text/event-stream`
- Conexión persistente (long-polling)
- Formato: `data: {json}\n\n`

**API JavaScript**:

```javascript
const eventSource = new EventSource("/endpoint");
eventSource.onmessage = (event) => {
  const data = JSON.parse(event.data);
  // Procesar datos
};
```

**API Spring Boot**:

```java
@GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public SseEmitter endpoint() {
  SseEmitter emitter = new SseEmitter(timeout);
  emitter.send(data);
  return emitter;
}
```

### 🔄 **Scope Session**

**¿Por qué es crucial?**

- Cada admin que se conecta tiene su propia instancia de `ControladorAdministrador`
- Cada instancia tiene su propia `ConexionNavegador`
- Cada conexión SSE es independiente
- Cuando un admin cierra sesión, solo su instancia se elimina

**Sin scope session**:

- Todos los admins compartirían la misma instancia (scope singleton)
- Solo habría 1 conexión SSE compartida
- No funcionaría correctamente con múltiples usuarios

## 🧪 Cómo Probar

### Escenario 1: Un administrador

```
1. Abre bonificaciones.html
2. Abre la consola del navegador (F12)
3. Verás: "Administrador conectado: [nombre]"
4. Asigna una bonificación
5. ¡Deberías ver la notificación aparecer!
```

### Escenario 2: Múltiples administradores

```
1. Abre 2 navegadores (o pestañas en modo incógnito)
2. Inicia sesión como admin en ambos
3. En el navegador 1:
   - Asigna una bonificación
4. En el navegador 2:
   - ¡Verás la notificación aparecer instantáneamente! 🎉
5. Verifica en la consola de ambos navegadores
```

### Verificación de la conexión SSE

```
1. Abre DevTools → Pestaña Network
2. Filtra por "registrarSSE"
3. Deberías ver una petición con:
   - Status: 200
   - Type: text/event-stream
   - Connection: keep-alive
4. La petición se mantiene abierta (no se cierra)
```

## 🐛 Debugging

### Logs del servidor

```java
// En ControladorAdministrador.actualizar()
System.out.println("Notificación enviada via SSE: Nueva asignación");

// En ConexionNavegador.enviarMensaje()
System.err.println("Error al enviar mensaje SSE: " + e.getMessage());
```

### Logs del cliente

```javascript
// En sse.js
console.log("Mensaje SSE recibido:", json);

// En bonificaciones.html
console.log("Administrador conectado:", parametro);
```

### Problemas comunes

**❌ "No recibo notificaciones"**

- Verifica que `urlRegistroSSE`, `urlIniciarVista`, `urlCierreVista` estén definidas
- Verifica que sse.js esté cargado DESPUÉS de vistaWeb.js
- Chequea la pestaña Network para ver si la conexión SSE está abierta
- Verifica en el servidor que el observador se registró correctamente

**❌ "La conexión SSE se cierra inmediatamente"**

- El timeout es 30 minutos, es normal que se cierre después
- Verifica que no haya excepciones en el servidor
- Chequea que `ConexionNavegador` esté con scope session

**❌ "Recibo notificaciones duplicadas"**

- Puede ser que haya múltiples instancias del observador registradas
- Verifica que vistaCerrada() se llame correctamente al salir

## 🚀 Extensiones Posibles

### 1. Diferentes tipos de notificaciones

```java
public enum Eventos {
    nuevaAsignacion,
    bonificacionEliminada,
    propietarioBloqueado,
    saldoBajo
}
```

### 2. Notificaciones personalizadas por admin

```java
@Override
public void actualizar(Object evento, Observable origen) {
    if (evento.equals(Fachada.Eventos.nuevaAsignacion)) {
        // Solo notificar si el admin tiene permisos
        if (tienePermiso(admin, "ver_asignaciones")) {
            conexionNavegador.enviarJSON(...);
        }
    }
}
```

### 3. Enviar datos completos en la notificación

```java
// En lugar de solo un mensaje, enviar toda la información
conexionNavegador.enviarJSON(
    Respuesta.lista(
        new Respuesta("notificacion", "Nueva asignación"),
        new Respuesta("propietario", propietario.getNombre()),
        new Respuesta("bonificacion", bonificacion.getNombre()),
        new Respuesta("puesto", puesto.getNombre())
    )
);
```

### 4. Notificaciones también para propietarios

```java
// Crear ControladorPropietario similar
// Notificar cuando se les asigna una bonificación a ellos
```

## 📚 Referencias

- [MDN: Server-Sent Events](https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events)
- [Spring Boot: SSE](https://spring.io/blog/2013/05/23/spring-mvc-4-0-server-sent-events)
- [Patrón Observer](https://refactoring.guru/design-patterns/observer)

---

**✨ ¡Implementación completa! ✨**

Ahora cuando un administrador asigna una bonificación, TODOS los administradores conectados reciben una notificación en tiempo real automáticamente.
