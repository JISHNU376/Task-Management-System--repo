import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private String description;
    private LocalDate dueDate;
    private int priority; // 1 = High, 2 = Medium, 3 = Low
    private boolean isCompleted;

    public Task(String description, LocalDate dueDate, int priority) {
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.isCompleted = false;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void markAsCompleted() {
        this.isCompleted = true;
    }

    @Override
    public String toString() {
        return "Task[Description=" + description + ", DueDate=" + dueDate + ", Priority=" + priority + ", Status=" + (isCompleted ? "Completed" : "Pending") + "]";
    }
}

class TaskManager {
    private List<Task> tasks;
    private static final String FILE_NAME = "tasks.dat";

    public TaskManager() {
        this.tasks = new ArrayList<>();
        loadTasks();
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
    }

    public void updateTask(int index, String description, LocalDate dueDate, int priority) {
        if (isValidIndex(index)) {
            Task task = tasks.get(index);
            task.setDescription(description);
            task.setDueDate(dueDate);
            task.setPriority(priority);
            saveTasks();
        }
    }

    public void deleteTask(int index) {
        if (isValidIndex(index)) {
            tasks.remove(index);
            saveTasks();
        }
    }

    public void markTaskAsCompleted(int index) {
        if (isValidIndex(index)) {
            tasks.get(index).markAsCompleted();
            saveTasks();
        }
    }

    public List<Task> getTasksSortedByPriority() {
        tasks.sort(Comparator.comparingInt(Task::getPriority));
        return tasks;
    }

    public List<Task> getTasksSortedByDueDate() {
        tasks.sort(Comparator.comparing(Task::getDueDate));
        return tasks;
    }

    public List<Task> getTasksDueWithinADay() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Task> dueTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted() && task.getDueDate().equals(tomorrow)) {
                dueTasks.add(task);
            }
        }
        return dueTasks;
    }

    public void displayTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
    }

    private boolean isValidIndex(int index) {
        if (index < 0 || index >= tasks.size()) {
            System.out.println("Invalid task index.");
            return false;
        }
        return true;
    }

    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadTasks() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                tasks = (List<Task>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading tasks: " + e.getMessage());
            }
        }
    }
}

public class TaskManagementSystem {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

        while (true) {
            System.out.println("\n--- Task Management System ---");
            System.out.println("1. Add Task");
            System.out.println("2. Update Task");
            System.out.println("3. Delete Task");
            System.out.println("4. Mark Task as Completed");
            System.out.println("5. Display Tasks");
            System.out.println("6. Sort Tasks by Priority");
            System.out.println("7. Sort Tasks by Due Date");
            System.out.println("8. View Tasks Due Within a Day");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter due date (yyyy-MM-dd): ");
                    LocalDate dueDate = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
                    System.out.print("Enter priority (1-High, 2-Medium, 3-Low): ");
                    int priority = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    taskManager.addTask(new Task(description, dueDate, priority));
                    break;
                case 2:
                    taskManager.displayTasks();
                    System.out.print("Enter task number to update: ");
                    int updateIndex = scanner.nextInt() - 1;
                    scanner.nextLine();
                    System.out.print("Enter new task description: ");
                    description = scanner.nextLine();
                    System.out.print("Enter new due date (yyyy-MM-dd): ");
                    dueDate = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
                    System.out.print("Enter new priority (1-High, 2-Medium, 3-Low): ");
                    priority = scanner.nextInt();
                    scanner.nextLine();
                    taskManager.updateTask(updateIndex, description, dueDate, priority);
                    break;
                case 3:
                    taskManager.displayTasks();
                    System.out.print("Enter task number to delete: ");
                    int deleteIndex = scanner.nextInt() - 1;
                    scanner.nextLine();
                    taskManager.deleteTask(deleteIndex);
                    break;
                case 4:
                    taskManager.displayTasks();
                    System.out.print("Enter task number to mark as completed: ");
                    int completeIndex = scanner.nextInt() - 1;
                    scanner.nextLine();
                    taskManager.markTaskAsCompleted(completeIndex);
                    break;
                case 5:
                    taskManager.displayTasks();
                    break;
                case 6:
                    System.out.println("\n--- Tasks Sorted by Priority ---");
                    taskManager.getTasksSortedByPriority().forEach(System.out::println);
                    break;
                case 7:
                    System.out.println("\n--- Tasks Sorted by Due Date ---");
                    taskManager.getTasksSortedByDueDate().forEach(System.out::println);
                    break;
                case 8:
                    System.out.println("\n--- Tasks Due Within a Day ---");
                    taskManager.getTasksDueWithinADay().forEach(System.out::println);
                    break;
                case 9:
                    System.out.println("Exiting Task Management System. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
