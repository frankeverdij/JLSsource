package jls;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class JavaLifeSearch implements Runnable{

    private static String fileToLoad = null;
    
    private static final String version = "1.7g";

    public void run()
    {
        JFrame frame = new JFrame("JavaLifeSearch v" + version);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent contentPane = new ContentPane(frame, fileToLoad);
        contentPane.setOpaque(true);
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            fileToLoad = args[0];
        }
        javax.swing.SwingUtilities.invokeLater(new JavaLifeSearch());
    }
}
