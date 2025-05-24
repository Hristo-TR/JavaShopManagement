package com.shop.ui;

public class MenuHandler {

    private final InputReader inputReader;
    private final OutputPrinter outputPrinter;

    public MenuHandler(InputReader inputReader, OutputPrinter outputPrinter) {
        this.inputReader = inputReader;
        this.outputPrinter = outputPrinter;
    }

    public int handleMainMenu() {
        outputPrinter.printMainMenu();
        return inputReader.readInt("Enter your choice: ", 0, 5);
    }

    public int handleInventoryMenu() {
        outputPrinter.printInventoryMenu();
        return inputReader.readInt("Enter your choice: ", 0, 4);
    }

    public int handleEmployeeMenu() {
        outputPrinter.printEmployeeMenu();
        return inputReader.readInt("Enter your choice: ", 0, 4);
    }

    public int handleSalesMenu() {
        outputPrinter.printSalesMenu();
        return inputReader.readInt("Enter your choice: ", 0, 2);
    }

    public int handleReportsMenu() {
        outputPrinter.printReportsMenu();
        return inputReader.readInt("Enter your choice: ", 0, 5);
    }

    public boolean confirmAction(String message) {
        return inputReader.readYesNo(message + " (y/n): ");
    }

    public void waitForEnter() {
        outputPrinter.print("Press ENTER to continue...");
        inputReader.readString("");
    }

    public void exitApplication() {
        outputPrinter.print("Adios!");
    }
} 