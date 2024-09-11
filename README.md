# Project Description:
-A 3 person **group project** for a **Software Engineering with Java module** in University. 

-Completed over a 10 week period along with other courseowrk.

-Please see **4 sprint submissions** and most importantly **Implementation and Testing** files which thoroughly documented project progress througout.

-Project can be run using **jdk 21** + or by downloading the jar file located in **out/artifacts/** or **final_submission** folder directory.

# Black Box Plus - Java Board Game Description

Black Box Plus is an exciting extension of the classic strategy game, Black Box, offering a unique and more challenging gameplay experience on a hexagonal board. In this game of deduction, players take on the role of scientists attempting to uncover the hidden positions of atoms inside a mysterious hexagonal grid by firing rays and analyzing how they react. Using logical reasoning and strategy, players must deduce the correct positions of all the atoms while minimizing their moves.
## Repo Navigation:

## Game Modes:

### Sandbox Mode

The **Sandbox** is an area for you to experiment with the game's mechanics and develop your own understanding of how it works.

#### ATOMS:
- In the Sandbox, you can **place** or **remove atoms** and clearly observe their **circle of influence** and how it affects rays.
- The atoms are displayed as **black circles** with a **red highlighted circle of influence**, which is used to deflect the rays.

#### RAYS:
- **Send rays** by clicking along the border closest to the side from which you want to launch the ray.
- To **remove a ray**, simply click on it again.
- **Absorbed rays** are marked in **black**, while other rays are marked with distinct colors based on their interactions.

---

### 2 Player Mode

The **Two Player Mode** is perfect for playing with friends. The goal is for Player 2 to find the hidden atoms placed by Player 1, with players switching roles at the end.

#### How It Works:

- **Player 1** starts by **strategically placing 6 atoms** on the grid. Once all atoms are placed, they get the option to **hide** the atoms.
- **Player 2's turn** begins next. Their task is to **find the hidden atoms** by sending rays from the border. Based on the ray entry and exit points, Player 2 can place **blue guess atoms** where they think the hidden atoms are located.
- Once Player 2 has placed all **6 guess atoms**, they can choose to **compare** their guess with Player 1's original placement.
- After the comparison, the players **switch roles**, with Player 2 placing atoms and Player 1 guessing, playing until both rounds are complete and the final scores are compared.

#### Comparison of Atoms:
- **Green**: Correctly guessed atoms.
- **Red**: Incorrectly guessed atoms.
- **Black**: Atoms that were not found.

---

### Single Player Mode

The **Single Player Mode** is ideal for mastering the game on your own. The goal is to find randomly allocated hidden atoms using deductive reasoning and strategy.

#### How It Works:

- Atoms are **randomly placed** and hidden at the start of the game.
- Strategically **send rays** to deduce the locations of the hidden atoms.
- Once you are confident, place **6 guess atoms** on the board where you believe the atoms are located.
- After placing all 6 guess atoms, compare your guesses to the actual hidden atoms to see your **resulting score**.

#### Comparison of Atoms:
- **Green**: Correctly guessed atoms.
- **Red**: Incorrectly guessed atoms.
- **Black**: Atoms that were not found.
## How to Launch the Game

### Launching from JAR File

The game’s JAR file is located in the **out/artifacts/ directory**. You can run the game without needing to compile it yourself by downloading this file. Follow these steps:

1. **Ensure you have Java installed**:

   You need to have **Java JDK 21** or higher installed on your machine. You can check if Java is installed by running the following command in your terminal or command prompt:
   ```
   java -version
If Java is not installed, you can download it from Oracle's website or OpenJDK.

2.**Navigate to the directory where the .jar file is located using the terminal or command prompt.**

3.**Run the JAR file by executing the following command:**
  
    java -jar GameName.jar
Replace GameName.jar with the actual name of your .jar file.

