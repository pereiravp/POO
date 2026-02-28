
#  Object-Oriented Programming (POO) - UMinho



![Java](https://img.shields.io/badge/Language-Java-orange) ![University](https://img.shields.io/badge/Institution-Universidade%20do%20Minho-blue) ![Status](https://img.shields.io/badge/Status-Active-brightgreen)



This repository hosts the practical coursework and resolutions for the **Programação Orientada a Objetos** (POO) curricular unit at the **University of Minho**. 



The main goal is to document my progress in mastering Java, Algorithmic Logic, and Software Engineering principles.



## Repository Structure



The project is organized by practical worksheets, covering the following topics:



| Worksheet | Topic | Key Concepts |

| :--- | :--- | :--- |

| **Ficha 1** | Introduction to Java | `Primitive Types`, `Control Flow`, `I/O` |

| **Ficha 2** | Arrays & Matrices | `Data Structures`, `Memory Management` |

| **Ficha 3** | Object Basics | `Classes`, `Encapsulation`, `Constructors` |



## Technologies



* **Language:** Java (JDK 17+)

* **IDE:** VS Code / IntelliJ IDEA
  

### Executing Worksheet 1 (Standalone Files)

The first worksheet consists of independent files for each exercise. To test these, navigate to the folder and compile the target file directly.

```bash
cd worksheet1
javac Ex1.java
java Ex1

```

### Executing Worksheet 2 (Centralized Architecture)

Worksheet 2 introduces a centralized execution model. All exercises are orchestrated through a single `Main.java` class to streamline compilation and avoid redundant code.

To evaluate a specific exercise, you must enable its execution call within the `main` method:

1. Open the `Main.java` file in your preferred code editor.
2. Locate the `public static void main(String[] args)` method.
3. Uncomment the specific method you wish to test by removing the double slashes (`//`). Ensure all other methods remain commented to prevent overlapping terminal outputs.

*Example configuration to test Exercise 7:*

```java
public static void main(String[] args) {
    // printEx5();
    // printEx6();
    printEx7(); 
}

```

4. Compile all Java files in the directory simultaneously:

```bash
cd worksheet2
javac *.java

```

5. Execute the Main class:

```bash
java Main

```

---

**Author:** Gonçalo Simões Pereira

*Computer Science Student at University of Minho*

```
