package sistemaMilitar;

import sistemaMilitar.DAOs.AsignacionDAO;
import sistemaMilitar.DAOs.CuartelDAO;
import sistemaMilitar.DAOs.PersonaDAO;
import sistemaMilitar.DAOs.ReservaDAO;
import sistemaMilitar.controllers.AsignacionController;
import sistemaMilitar.controllers.CuartelController;
import sistemaMilitar.controllers.PersonaController;
import sistemaMilitar.controllers.ReservaController;
import sistemaMilitar.services.AsignacionService;
import sistemaMilitar.services.CuartelService;
import sistemaMilitar.services.PersonaService;
import sistemaMilitar.services.ReservaService;
import sistemaMilitar.views.AsignacionView;
import sistemaMilitar.views.CuartelView;
import sistemaMilitar.views.MenuPrincipalView;
import sistemaMilitar.views.OficialView;
import sistemaMilitar.views.ReservaView;
import sistemaMilitar.views.SoldadoView;

public class Main {

    public static void main(String[] args) {

        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        System.setErr(new java.io.PrintStream(System.err, true, java.nio.charset.StandardCharsets.UTF_8));

        // 1. Instanciación de DAOs
        CuartelDAO cuartelDAO = new CuartelDAO();
        PersonaDAO personaDAO = new PersonaDAO();
        ReservaDAO reservaDAO = new ReservaDAO();
        AsignacionDAO asignacionDAO = new AsignacionDAO();

        // 2. Instanciación de Servicios (Inyección de DAOs)
        CuartelService cuartelService = new CuartelService(cuartelDAO);
        PersonaService personaService = new PersonaService(personaDAO);
        ReservaService reservaService = new ReservaService(reservaDAO, cuartelDAO, personaDAO);
        AsignacionService asignacionService = new AsignacionService(asignacionDAO, cuartelDAO, personaDAO);

        // 3. Instanciación de Controladores (Inyección de Servicios)
        CuartelController cuartelController = new CuartelController(cuartelService);
        PersonaController personaController = new PersonaController(personaService);
        ReservaController reservaController = new ReservaController(reservaService);
        AsignacionController asignacionController = new AsignacionController(asignacionService);

        // 4. Instanciación de Vistas (Inyección de Controladores)
        CuartelView cuartelView = new CuartelView(cuartelController);
        SoldadoView soldadoView = new SoldadoView(personaController);
        OficialView oficialView = new OficialView(personaController);
        ReservaView reservaView = new ReservaView(reservaController);
        AsignacionView asignacionView = new AsignacionView(asignacionController);

        // 5. Iniciar la aplicación
        MenuPrincipalView menuPrincipal = new MenuPrincipalView(
                cuartelView,
                soldadoView,
                oficialView,
                reservaView,
                asignacionView
        );

        menuPrincipal.iniciar();
    }
}
