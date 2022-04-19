/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utilities;

import hr.algebra.engine.controller.ChessController;
import hr.algebra.engine.model.board.Board;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;

/**
 *
 * @author frand
 */
public class ReflectionUtils {
    
    public static void readClassInfo(Class<?> clazz, StringBuilder classInfo, boolean first) {
        if (first) {
            appendPackage(clazz, classInfo);
        }
        classInfo.append("<h3>&emsp;&emsp;");
        appendModifiers(clazz, classInfo);
        classInfo.append(" ").append(clazz.getSimpleName());
        classInfo.append(" ");
        appendParent(clazz, classInfo, true);
        classInfo.append(" ");
        appendInterfaces(clazz, classInfo);
        classInfo.append("</h3>");
    }
    
    private static void appendPackage(Class<?> clazz, StringBuilder classInfo) {
        
        classInfo.append("<h3>&emsp;&emsp;").append(clazz.getPackage()).append("</h3>");
    }

    private static void appendModifiers(Class<?> clazz, StringBuilder classInfo) {
        int mod = clazz.getModifiers();
       classInfo.append(Modifier.toString(mod));
    }

    private static void appendParent(Class<?> clazz, StringBuilder classInfo, boolean first) {
        Class<?> superclass = clazz.getSuperclass();
        if (superclass == null) {
            return;
        }
        if (first) {
            classInfo.append("extends");
        }
        classInfo.append(" ").append(superclass.getSimpleName());
        appendParent(superclass, classInfo, false);
    }

    private static void appendInterfaces(Class<?> clazz, StringBuilder classInfo) {
        if (clazz.getInterfaces().length > 0) {
            classInfo.append("implements ");
            classInfo.append(
                    Arrays.stream(clazz.getInterfaces())
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining(" "))
            );
                    
        }
    }

    public static void ReadMembersInfo(Class<?> clazz, StringBuilder classAndMembersInfo, boolean first) {
        readClassInfo(clazz, classAndMembersInfo, first);
        appendFields(clazz, classAndMembersInfo);
        appendMethods(clazz, classAndMembersInfo);
        appendConstructors(clazz, classAndMembersInfo);
        classAndMembersInfo.append("<hr>");
        appendClasses(clazz, classAndMembersInfo);
    }

    private static void appendFields(Class<?> clazz, StringBuilder classAndMembersInfo) {
        Field [] fields = clazz.getDeclaredFields();
        classAndMembersInfo.append("<h3>&emsp;&emsp;Fields:</h3>");
        classAndMembersInfo.append(
                Arrays.stream(fields)
                .map(Objects::toString)
                .collect(Collectors.joining("</br>&emsp;&emsp;&emsp;&emsp;", "&emsp;&emsp;&emsp;&emsp;", "</br>"))
        );
        
    }

    private static void appendMethods(Class<?> clazz, StringBuilder classAndMembersInfo) {
        Method[] methods = clazz.getDeclaredMethods();
        classAndMembersInfo.append("<h3>&emsp;&emsp;Methods:</h3>");
        for (Method method : methods) {
            classAndMembersInfo.append("&emsp;&emsp;&emsp;&emsp;");
            appendAnnotations(method, classAndMembersInfo);
            classAndMembersInfo
                    .append(Modifier.toString(method.getModifiers()))
                    .append(" ")
                    .append(method.getReturnType())
                    .append(" ")
                    .append(method.getName());
            appendParameters(method, classAndMembersInfo);
            appendExceptions(method, classAndMembersInfo);
            classAndMembersInfo.append("</br>");
        }
    }
    private static void appendAnnotations(Executable executable, StringBuilder classAndMembersInfo) {
        classAndMembersInfo.append(
                Arrays.stream(executable.getAnnotations())
                        .map(Objects::toString)
                        .collect(Collectors.joining("\n")));
    }

    private static void appendParameters(Executable executable, StringBuilder classAndMembersInfo) {
        classAndMembersInfo.append(
                Arrays.stream(executable.getParameters())
                        .map(Objects::toString)
                        .collect(Collectors.joining(", ", "(", ")"))
        );
    }

    private static void appendExceptions(Executable executable, StringBuilder classAndMembersInfo) {
        if (executable.getExceptionTypes().length > 0) {
            classAndMembersInfo.append(" throws ");
            classAndMembersInfo.append(
                    Arrays.stream(executable.getExceptionTypes())
                            .map(Class::getName)
                            .collect(Collectors.joining(" "))
            );
        }
    }

    private static void appendConstructors(Class<?> clazz, StringBuilder classAndMembersInfo) {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        classAndMembersInfo.append("<h3>&emsp;&emsp;Constructors:</h3>");
        for (Constructor constructor : constructors) {
            classAndMembersInfo.append("&emsp;&emsp;&emsp;&emsp;");
            appendAnnotations(constructor, classAndMembersInfo);
            classAndMembersInfo
                    .append("\n")
                    .append(Modifier.toString(constructor.getModifiers()))
                    .append(" ")
                    .append(constructor.getName());
            appendParameters(constructor, classAndMembersInfo);
            appendExceptions(constructor, classAndMembersInfo);
            classAndMembersInfo.append("</br>");
        }   
    }

    private static void appendClasses(Class<?> clazz, StringBuilder classAndMembersInfo) {
        Class[] classes = clazz.getDeclaredClasses();
        for (Class c : classes) {
            ReadMembersInfo(c, classAndMembersInfo, false);
        }   
    }
    
    public static void writeDocumentation() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("<!DOCTYPE html>\n");
        builder.append("<html>\n");
        builder.append("<head>\n");
        builder.append("<title>Dokumentacija</title>\n");
        builder.append("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3\" crossorigin=\"anonymous\">\n");
        builder.append("</head>\n");
        builder.append("<body style=\"background-color:lightgray; margin:50px\">\n");
        builder.append("<h1 style=\"text-align:center; font-size:3em\">Osnovni podaci o paketima</h1>\n");
        builder.append("<div id=\"carouselExampleControls\" class=\"carousel slide\" data-bs-ride=\"carousel\">\n" +
                "  <div class=\"carousel-inner\">\n");
        
        String packageLocation = ".\\src";
        writePackeges(packageLocation, "", builder, true);

        builder.append("</div>\n" +
                "  <button class=\"carousel-control-prev\" style=\"height:100vh; position:fixed; left:-65px\" type=\"button\" data-bs-target=\"#carouselExampleControls\" data-bs-slide=\"prev\">\n" +
                "    <span class=\"carousel-control-prev-icon\" aria-hidden=\"true\"></span>\n" +
                "    <span class=\"visually-hidden\">Previous</span>\n" +
                "  </button>\n" +
                "  <button class=\"carousel-control-next\" style=\"height:100vh; position:fixed; right:-65px\" type=\"button\" data-bs-target=\"#carouselExampleControls\" data-bs-slide=\"next\">\n" +
                "    <span class=\"carousel-control-next-icon\" aria-hidden=\"true\"></span>\n" +
                "    <span class=\"visually-hidden\">Next</span>\n" +
                "  </button>\n" +
                "</div>\n");
        builder.append("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p\" crossorigin=\"anonymous\"></script>\n");
        builder.append("</body>\n");
        builder.append("</html>\n");

        try (FileWriter zapisivac = new FileWriter("dokumentacija.html")) {
            zapisivac.write(builder.toString());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspješno generiranje dokumentacije");
            alert.setHeaderText("Dokumentacija za Vaš kod je uspješno"
                    + "generirana!");
            alert.setContentText("Datoteka \"dokumentacija.html\""
                    + "je uspješno generirana!");

            alert.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(ChessController.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Neuspješno generiranje dokumentacije");
            alert.setHeaderText("Dokumentacija za Vaš kod nije "
                    + "generirana!");
            alert.setContentText("Datoteka \"dokumentacija.html\""
                    + "nije se kreirala zbog pogreške u sustavu!");

            alert.showAndWait();
        }
    }

    private static void writePackeges(String packageLocation, String packageLocation2, StringBuilder builder, boolean first) {
        String packageArray[] = new File(packageLocation).list();

        for (String packageName : packageArray) {

            String childPackageArray[] = new File(packageLocation + "\\" + packageName).list();
            //System.out.println("Package name: " + packageName);
            if (!packageName.contains(".")) {
                builder.append(first ? "<div class=\"carousel-item active\">" : "<div class=\"carousel-item\">")
                        .append("<h1 style=\"text-align:center\">")
                        .append(packageName)
                        .append(" (")
                        .append(packageLocation2 == "" ? packageName : packageLocation2 + "." + packageName)
                        .append(")")
                        .append("</h1>\n");

            }

            String classArray[] = new File(packageLocation + "\\"
                    + packageName).list();
            //            System.out.println("Class names:");
            if (classArray != null) {

                for (String className : classArray) {

                    if (className.endsWith(".java") == false) {
                        continue;
                    }
                    try {
                        Class c = Class.forName(
                                packageLocation2 + "." + packageName + "."
                                + className.substring(0, className.indexOf(".")));
                        builder.append("<h2>&emsp;Naziv klase: ")
                                .append(className)
                                .append("</h2>\n");
                        ReadMembersInfo(c, builder, true);

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ChessController.class.getName()).log(
                                Level.SEVERE, null, ex);
                    }
                }
            }
            if (!packageName.contains(".")) {
                builder.append("</div>");
            }
            
            if (childPackageArray != null) {
                writePackeges(packageLocation + "\\" + packageName, packageLocation2 == "" ? packageName : packageLocation2 + "." + packageName, builder, false);
            }
        }
    }
}
