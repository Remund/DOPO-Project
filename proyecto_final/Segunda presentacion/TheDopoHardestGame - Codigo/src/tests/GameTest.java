package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.BasicEnemy;
import domain.Game;
import domain.GameException;
import domain.Level;
import domain.LinearMovement;
import domain.RedPlayer;
import domain.YellowCoin;

public class GameTest {

    // Variables compartidas entre pruebas
    private RedPlayer jugador;
    private Level nivel;
    private Game juego;

    // =============================================
    // BeforeEach: se ejecuta antes de cada prueba
    // =============================================
    @BeforeEach
    public void setUp() {
        jugador = new RedPlayer(100, 100);
        nivel   = new Level(60);
        juego   = new Game(nivel, jugador);
    }

    // =============================================
    // PRUEBAS: RedPlayer
    // =============================================

    @Test
    public void testRedPlayerVelocidadEsUno() {
        assertEquals(1.0, jugador.getSpeed(),
            "La velocidad del jugador rojo debe ser 1.0");
    }

    @Test
    public void testRedPlayerTamanioEsUno() {
        assertEquals(1.0, jugador.getSize(),
            "El tamanio del jugador rojo debe ser 1.0");
    }

    @Test
    public void testRedPlayerPosicionInicial() {
        assertEquals(100, jugador.getX(),
            "La posicion X inicial debe ser 100");
        assertEquals(100, jugador.getY(),
            "La posicion Y inicial debe ser 100");
    }

    @Test
    public void testRedPlayerMuertesSonCeroAlInicio() {
        assertEquals(0, jugador.getDeaths(),
            "Las muertes deben ser 0 al iniciar");
    }

    @Test
    public void testRedPlayerColisionSumaUnaMuerte() {
        jugador.onEnemyCollision();
        assertEquals(1, jugador.getDeaths(),
            "Despues de una colision debe tener 1 muerte");
    }

    @Test
    public void testRedPlayerColisionVariasMuertes() {
        jugador.onEnemyCollision();
        jugador.onEnemyCollision();
        jugador.onEnemyCollision();
        assertEquals(3, jugador.getDeaths(),
            "Despues de tres colisiones debe tener 3 muertes");
    }

    @Test
    public void testRedPlayerRespawnVuelveAlSpawn() {
        // Mover al jugador
        jugador.move(50, 50);
        // Simular colision, debe volver al spawn
        jugador.onEnemyCollision();
        assertEquals(100, jugador.getX(),
            "Despues de morir debe volver a X inicial");
        assertEquals(100, jugador.getY(),
            "Despues de morir debe volver a Y inicial");
    }

    @Test
    public void testRedPlayerMovimiento() {
        jugador.move(10, 0);
        assertEquals(110, jugador.getX(),
            "El jugador debe moverse 10 en X");
    }

    @Test
    public void testRedPlayerMovimientoDiagonal() {
        jugador.move(5, 5);
        assertEquals(105, jugador.getX(),
            "El jugador debe moverse 5 en X diagonal");
        assertEquals(105, jugador.getY(),
            "El jugador debe moverse 5 en Y diagonal");
    }

    // =============================================
    // PRUEBAS: YellowCoin
    // =============================================

    @Test
    public void testMonedaNoRecolectadaAlInicio() {
        YellowCoin moneda = new YellowCoin(200, 200);
        assertFalse(moneda.isCollected(),
            "La moneda no debe estar recolectada al inicio");
    }

    @Test
    public void testMonedaActivaAlInicio() {
        YellowCoin moneda = new YellowCoin(200, 200);
        assertTrue(moneda.isActive(),
            "La moneda debe estar activa al inicio");
    }

    @Test
    public void testMonedaRecolectada() {
        YellowCoin moneda = new YellowCoin(200, 200);
        moneda.collect(jugador);
        assertTrue(moneda.isCollected(),
            "La moneda debe estar recolectada despues de collect()");
    }

    @Test
    public void testMonedaInactivaDespuesDeRecolectar() {
        YellowCoin moneda = new YellowCoin(200, 200);
        moneda.collect(jugador);
        assertFalse(moneda.isActive(),
            "La moneda debe estar inactiva despues de recolectarse");
    }

    // =============================================
    // PRUEBAS: BasicEnemy con LinearMovement
    // =============================================

    @Test
    public void testEnemigoPosicionInicial() {
        BasicEnemy enemigo = new BasicEnemy(300, 200);
        assertEquals(300, enemigo.getX(),
            "La posicion X inicial del enemigo debe ser 300");
        assertEquals(200, enemigo.getY(),
            "La posicion Y inicial del enemigo debe ser 200");
    }

    @Test
    public void testEnemigoSeMueveConestrategia() {
        BasicEnemy enemigo = new BasicEnemy(300, 200);
        enemigo.setMovementStrategy(new LinearMovement(2, 0));
        enemigo.update();
        assertEquals(302, enemigo.getX(),
            "El enemigo debe moverse 2px a la derecha con LinearMovement(2,0)");
    }

    @Test
    public void testEnemigoSinEstrategiaNoSemueve() {
        BasicEnemy enemigo = new BasicEnemy(300, 200);
        // Sin estrategia asignada
        enemigo.update();
        assertEquals(300, enemigo.getX(),
            "Sin estrategia el enemigo no debe moverse en X");
        assertEquals(200, enemigo.getY(),
            "Sin estrategia el enemigo no debe moverse en Y");
    }

    @Test
    public void testEstrategiaIntercambiable() {
        // Patrón Strategy: se puede cambiar la estrategia en tiempo de ejecución
        BasicEnemy enemigo = new BasicEnemy(300, 200);

        // Primera estrategia: va a la derecha
        enemigo.setMovementStrategy(new LinearMovement(2, 0));
        enemigo.update();
        assertEquals(302, enemigo.getX(),
            "Con estrategia horizontal debe moverse en X");

        // Cambiar estrategia: ahora va hacia abajo
        enemigo.setMovementStrategy(new LinearMovement(0, 3));
        enemigo.update();
        assertEquals(203, enemigo.getY(),
            "Con estrategia vertical debe moverse en Y");
    }

    // =============================================
    // PRUEBAS: Level
    // =============================================

    @Test
    public void testNivelNoCompletadoSinMonedas() {
        YellowCoin moneda = new YellowCoin(200, 200);
        nivel.addCoin(moneda);
        assertFalse(nivel.isCompleted(),
            "El nivel no debe estar completo si hay monedas sin recolectar");
    }

    @Test
    public void testNivelCompletadoConTodasLasMonedas() {
        YellowCoin moneda1 = new YellowCoin(200, 200);
        YellowCoin moneda2 = new YellowCoin(300, 200);
        nivel.addCoin(moneda1);
        nivel.addCoin(moneda2);

        moneda1.collect(jugador);
        moneda2.collect(jugador);

        assertTrue(nivel.isCompleted(),
            "El nivel debe estar completo cuando todas las monedas estan recolectadas");
    }

    @Test
    public void testNivelMonedasRestantes() {
        YellowCoin moneda1 = new YellowCoin(200, 200);
        YellowCoin moneda2 = new YellowCoin(300, 200);
        YellowCoin moneda3 = new YellowCoin(400, 200);
        nivel.addCoin(moneda1);
        nivel.addCoin(moneda2);
        nivel.addCoin(moneda3);

        moneda1.collect(jugador);

        assertEquals(2, nivel.getRemainingCoins(),
            "Deben quedar 2 monedas por recolectar");
    }

    @Test
    public void testNivelTiempoLimite() {
        assertEquals(60, nivel.getTimeLimit(),
            "El tiempo limite del nivel debe ser 60");
    }

    // =============================================
    // PRUEBAS: Game
    // =============================================

    @Test
    public void testJuegoNoCorreDespuesDeCrearse() {
        assertFalse(juego.isRunning(),
            "El juego no debe estar corriendo al crearse");
    }

    @Test
    public void testJuegoCorreDespuesDeStart() {
        juego.start();
        assertTrue(juego.isRunning(),
            "El juego debe estar corriendo despues de start()");
    }

    @Test
    public void testJuegoSePausa() {
        juego.start();
        juego.pause();
        assertTrue(juego.isPaused(),
            "El juego debe estar pausado despues de pause()");
    }

    @Test
    public void testJuegoReset() {
        juego.start();
        jugador.move(50, 50);
        juego.reset();
        assertEquals(100, jugador.getX(),
            "Despues de reset el jugador debe volver a X inicial");
        assertEquals(100, jugador.getY(),
            "Despues de reset el jugador debe volver a Y inicial");
    }

    @Test
    public void testMoverJugadorSinIniciarLanzaExcepcion() {
        // El juego no esta iniciado, debe lanzar GameException
        assertThrows(GameException.class, () -> {
            juego.moverJugador(5, 0);
        }, "Mover jugador sin iniciar el juego debe lanzar GameException");
    }

    @Test
    public void testMoverJugadorPausadoLanzaExcepcion() {
        juego.start();
        juego.pause();
        assertThrows(GameException.class, () -> {
            juego.moverJugador(5, 0);
        }, "Mover jugador con juego pausado debe lanzar GameException");
    }

    @Test
    public void testTiempoRestanteDisminuye() {
        juego.start();
        juego.actualizarTiempo();
        assertEquals(59, juego.getTiempoRestante(),
            "El tiempo restante debe disminuir en 1 despues de actualizarTiempo()");
    }

    @Test
    public void testTiempoNoDisminyueSiPausado() {
        juego.start();
        juego.pause();
        juego.actualizarTiempo();
        assertEquals(60, juego.getTiempoRestante(),
            "El tiempo no debe disminuir si el juego esta pausado");
    }

    @Test
    public void testCheckCollisionsMonedaRecolectada() {
        // Poner moneda en la misma posicion del jugador
        YellowCoin moneda = new YellowCoin(100, 100);
        nivel.addCoin(moneda);
        juego.start();
        juego.checkCollisions();
        assertTrue(moneda.isCollected(),
            "La moneda debe recolectarse cuando el jugador esta encima");
    }

    @Test
    public void testCheckCollisionsEnemigoMataaJugador() {
        // Poner enemigo en la misma posicion del jugador
        BasicEnemy enemigo = new BasicEnemy(100, 100);
        nivel.addEnemy(enemigo);
        juego.start();
        juego.checkCollisions();
        assertEquals(1, jugador.getDeaths(),
            "El jugador debe sumar una muerte al chocar con enemigo");
    }
}