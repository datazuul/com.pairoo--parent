package com.pairoo.frontend.webapp;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.NCSARequestLog;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.RequestLogHandler;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * If execution of this class does not work:
 *
 * $ cd com.pairoo--parent/frontend/webapp
 *
 * $ mvn -Djetty.port=18080 jetty:run
 *
 * @author Ralf Eichinger
 */
public class Start {

    public static Server server = null;

    public static void main(final String[] args) throws Exception {
        // CATALINA_OPTS="-Denv=PROD -DsystemEnv.pairoo.payoneMode=live
        // -DsysEnv.pairoo.logfilename=pairoo-node2.log
        // -DsysEnv.pairoo.exception.receiver=exception@pairoo.com
        // -Xmx600m"
        if (System.getProperty("env") == null) {
            System.setProperty("env", "DEMO");
        }
        System.setProperty("wicket.configuration", "development");
                
        System.setProperty("sysEnv.pairoo.logfilename", "pairoo-demo.log");
        System.setProperty("sysEnv.pairoo.exception.receiver", "ralf.eichinger@pairoo.de");
        // if (System.getProperty("sysEnv.pairoo.payoneMode") == null) {
        System.setProperty("sysEnv.pairoo.payoneMode", "test");
        // }
        server = new Server();

        final SocketConnector connector = new SocketConnector();
        // Set some timeout options to make debugging easier.
        // connector.setMaxIdleTime(1000 * 60 * 60);
        connector.setSoLingerTime(-1);
        connector.setPort(28080);
        server.setConnectors(new Connector[]{connector});

        final WebAppContext bb = new WebAppContext();
        bb.setServer(server);
        bb.setContextPath("/portal");
        bb.setWar("src/main/webapp");

        bb.getSessionHandler().getSessionManager().setMaxInactiveInterval(30); // sets
        // to
        // 30
        // sec

        // ContextHandler staticContext = new ContextHandler("/static");
        // staticContext.setResourceBase("src/main");
        // staticContext.addHandler(new ResourceHandler());

        // final WebAppContext ssi = new WebAppContext();
        // ssi.setServer(server);
        // ssi.setContextPath("/static");
        // ssi.setWar("src/main/webapp");
        // ssi.setResourceBase("src/main");

        // // scanning for changes to reload the server:
        // final List<File> scanDirs = new ArrayList<File>();
        // // scan "src/main"
        // final File srcMain =
        // bb.getWebInf().getFile().getParentFile().getParentFile();
        // scanDirs.add(srcMain);

        // System.out.println("scanning " + srcMain.getAbsolutePath());

        // final Scanner scanner = new Scanner();
        // scanner.setReportExistingFilesOnStartup(false);
        // scanner.setScanInterval(10); // or make this configurable
        // scanner.setScanDirs(scanDirs);
        // scanner.setRecursive(true);
        //
        // final Scanner.Listener listener = new Scanner.BulkListener() {
        // @Override
        // public void filesChanged(final List changes) {
        // System.out.println("restarting webapp...");
        // try {
        // Start.server.stop();
        // Start.server.start();
        // } catch (final Exception e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // System.out.println("done.");
        // }
        // };
        //
        // scanner.addListener(listener);
        // System.out.println("starting scanner...");
        // scanner.start();

        // START JMX SERVER
        // MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        // MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
        // server.getContainer().addEventListener(mBeanContainer);
        // mBeanContainer.start();

        final RequestLogHandler requestLogHandler = new RequestLogHandler();
        final NCSARequestLog requestLog = new NCSARequestLog("/tmp/jetty-yyyy_mm_dd.request.log");
        requestLog.setRetainDays(90);
        requestLog.setAppend(true);
        requestLog.setExtended(false);
        requestLog.setLogTimeZone("GMT+1");
        requestLogHandler.setRequestLog(requestLog);

        server.addHandler(bb);
        // server.addHandler(ssi);
        server.addHandler(requestLogHandler);

        // start HSQLDB-Manager to inspect in memory database
        // only for demo
        if ("DEMO".equals(System.getProperty("env"))) {
            final String[] databaseManagerOptions = new String[]{"--url", "jdbc:hsqldb:mem:test", "--noexit"};
            org.hsqldb.util.DatabaseManagerSwing.main(databaseManagerOptions);
        }

        try {
            System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
            server.start();
            System.in.read();
            System.out.println(">>> STOPPING EMBEDDED JETTY SERVER");
            // while (System.in.available() == 0) {
            // Thread.sleep(5000);
            // }
            server.stop();
            server.join();
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }
}
