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
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;
import javax.swing.JFileChooser;

/**
 *
 * @author Kevin
 */
public class Principal {
    public static void main(String[] args) throws Exception {
        String ruta1 = "src/codigo/Lexer.flex";
        String ruta2 = "src/codigo/LexerCup.flex";
        String[] rutaS = {"-parser", "Sintax", "src/codigo/sintax.cup"};
        generar(ruta1, ruta2, rutaS);
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        analizador(chooser);
        parser(chooser);
    }
    
    public static void generar(String ruta1, String ruta2, String[] rutaS) throws IOException, Exception {
        File archivo;
        archivo = new File(ruta1);
        JFlex.Main.generate(archivo);
        archivo = new File(ruta2);
        JFlex.Main.generate(archivo);
        java_cup.Main.main(rutaS);
        
        Path rutaSym = Paths.get("src/codigo/sym.java");
        if (Files.exists(rutaSym)) {
            Files.delete(rutaSym);
        }
        Files.move(
                Paths.get("sym.java"),
                Paths.get("src/codigo/sym.java")
        );
        Path rutaSin = Paths.get("src/codigo/Sintax.java");
        if (Files.exists(rutaSin)) {
            Files.delete(rutaSin);
        }
        Files.move(
                Paths.get("Sintax.java"),
                Paths.get("src/codigo/Sintax.java")
        );
        
    }
    
    private static void analizador(JFileChooser chooser) throws Exception{
       
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
                    case Comillas: case T_dato: case Cadena: case If: case Else: case Do: case While:
                    case For: case Igual: case Suma: case Resta: case Multiplicacion: case Division:
                    case Op_logico: case Op_relacional: case Op_atribucion: case Op_incremento:
                    case Op_booleano: case Parentesis_a: case Parentesis_c: case Llave_a: case Llave_c:
                    case Corchete_a: case Corchete_c: case Main: case P_coma: case Identificador:
                    case Literal: case Numero: case Break: case Case: case Const: case Continue: 
                    case Default: case Return: case Switch: case Void:
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
    
    private static void parser(JFileChooser chooser) throws IOException {
        String resultado = " ";
        File archivo = new File(chooser.getSelectedFile().getAbsolutePath());
        String ST = new String(Files.readAllBytes(archivo.toPath()));
        Sintax s = new Sintax(new codigo.LexerCup(new StringReader(ST)));
        try {
            s.parse();
            System.out.println("Analisis realizado correctamente.");
        } catch (Exception ex) {
            Symbol sym = s.getS();
            resultado += ("Error de sintaxis. Linea: " + (sym.right + 1) + " Columna: " + (sym.left +1) + ", Texto: \"" + sym.value + "\"" + "\n");
        }
        System.out.println(resultado);
    }
}
