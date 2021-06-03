/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author Kevin
 */
public class Principal {
    public static void main(String[] args) {
        String ruta = "src/codigo/Lexer.flex";
        generarLexer(ruta);
        
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        
        int cont = 1;
        
        try {
            Reader lector = new BufferedReader(new FileReader(chooser.getSelectedFile()));
            Lexer lexer = new Lexer(lector);
            String resultado = "Linea " + cont + "\t\tSimbolo\n";
            while (true) {
                Tokens tokens = lexer.yylex();
                if (tokens == null) {
                    resultado += "FIN";
                    System.out.println(resultado);
                    break;
                }
                switch (tokens) {
                    case ERROR:
                        resultado += "Simbolo no definido\n";
                        break;
                    case Linea:
                        cont++;
                        resultado += "Linea " + cont + "\n";
                        break;
                    case Identificador: case Literal: case Reservadas: case Operador: case Delimitador:
                        resultado += "  " + tokens + "\t" + lexer.lexeme + "\n";
                        break;
                    default:
                        resultado += "  " + tokens + "\n";
                        break;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void generarLexer(String ruta) {
        File archivo = new File(ruta);
        JFlex.Main.generate(archivo);
    }
}
