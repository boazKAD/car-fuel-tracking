package com.fuel.tracking.cli;

import org.apache.commons.cli.*;

public class CommandParser {
    private final Options options;

    public CommandParser() {
        options = new Options();

        // Create car command
        OptionGroup createCarGroup = new OptionGroup();
        createCarGroup.addOption(Option.builder()
                .longOpt("create-car")
                .desc("Create a new car")
                .hasArg(false)
                .build());

        Option brandOption = Option.builder()
                .longOpt("brand")
                .desc("Car brand")
                .hasArg()
                .argName("BRAND")
                .build();

        Option modelOption = Option.builder()
                .longOpt("model")
                .desc("Car model")
                .hasArg()
                .argName("MODEL")
                .build();

        Option yearOption = Option.builder()
                .longOpt("year")
                .desc("Car year")
                .hasArg()
                .argName("YEAR")
                .type(Integer.class)
                .build();

        // Add fuel command
        OptionGroup addFuelGroup = new OptionGroup();
        addFuelGroup.addOption(Option.builder()
                .longOpt("add-fuel")
                .desc("Add fuel entry for a car")
                .hasArg(false)
                .build());

        Option carIdOption = Option.builder()
                .longOpt("carId")
                .desc("Car ID")
                .hasArg()
                .argName("ID")
                .type(Long.class)
                .build();

        Option litersOption = Option.builder()
                .longOpt("liters")
                .desc("Fuel liters")
                .hasArg()
                .argName("LITERS")
                .type(Double.class)
                .build();

        Option priceOption = Option.builder()
                .longOpt("price")
                .desc("Price per liter")
                .hasArg()
                .argName("PRICE")
                .type(Double.class)
                .build();

        Option odometerOption = Option.builder()
                .longOpt("odometer")
                .desc("Odometer reading")
                .hasArg()
                .argName("ODOMETER")
                .type(Integer.class)
                .build();

        // Fuel stats command
        OptionGroup fuelStatsGroup = new OptionGroup();
        fuelStatsGroup.addOption(Option.builder()
                .longOpt("fuel-stats")
                .desc("Get fuel statistics for a car")
                .hasArg(false)
                .build());

        // Help command
        Option helpOption = Option.builder("h")
                .longOpt("help")
                .desc("Show help")
                .hasArg(false)
                .build();

        // Add all options
        options.addOptionGroup(createCarGroup);
        options.addOption(brandOption);
        options.addOption(modelOption);
        options.addOption(yearOption);

        options.addOptionGroup(addFuelGroup);
        options.addOption(carIdOption);
        options.addOption(litersOption);
        options.addOption(priceOption);
        options.addOption(odometerOption);

        options.addOptionGroup(fuelStatsGroup);
        options.addOption(helpOption);
    }

    public Command parse(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Command command = new Command();

        if (cmd.hasOption("help") || args.length == 0) {
            command.setType(CommandType.HELP);
        } else if (cmd.hasOption("create-car")) {
            command.setType(CommandType.CREATE_CAR);
            command.setBrand(cmd.getOptionValue("brand"));
            command.setModel(cmd.getOptionValue("model"));
            command.setYear(cmd.getOptionValue("year"));
        } else if (cmd.hasOption("add-fuel")) {
            command.setType(CommandType.ADD_FUEL);
            command.setCarId(cmd.getOptionValue("carId"));
            command.setLiters(cmd.getOptionValue("liters"));
            command.setPrice(cmd.getOptionValue("price"));
            command.setOdometer(cmd.getOptionValue("odometer"));
        } else if (cmd.hasOption("fuel-stats")) {
            command.setType(CommandType.FUEL_STATS);
            command.setCarId(cmd.getOptionValue("carId"));
        } else {
            throw new ParseException("Unknown command. Use --help for usage.");
        }

        return command;
    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);

        String header = "\nCar Fuel Tracking CLI\n\n";
        String footer = "\nExamples:\n" +
                "  create-car --brand Toyota --model Corolla --year 2018\n" +
                "  add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000\n" +
                "  fuel-stats --carId 1\n";

        formatter.printHelp("cli", header, options, footer, true);
    }

    /**
     * Command types
     */
    public enum CommandType {
        HELP,
        CREATE_CAR,
        ADD_FUEL,
        FUEL_STATS
    }

    /**
     * Command data holder
     */
    public static class Command {
        private CommandType type;
        private String brand;
        private String model;
        private String year;
        private String carId;
        private String liters;
        private String price;
        private String odometer;

        // Getters and Setters
        public CommandType getType() {
            return type;
        }

        public void setType(CommandType type) {
            this.type = type;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getCarId() {
            return carId;
        }

        public void setCarId(String carId) {
            this.carId = carId;
        }

        public String getLiters() {
            return liters;
        }

        public void setLiters(String liters) {
            this.liters = liters;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOdometer() {
            return odometer;
        }

        public void setOdometer(String odometer) {
            this.odometer = odometer;
        }

        // Validation methods
        public Long getCarIdAsLong() throws IllegalArgumentException {
            try {
                return Long.parseLong(carId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid car ID: " + carId);
            }
        }

        public Integer getYearAsInt() throws IllegalArgumentException {
            try {
                return Integer.parseInt(year);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid year: " + year);
            }
        }

        public Double getLitersAsDouble() throws IllegalArgumentException {
            try {
                return Double.parseDouble(liters);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid liters: " + liters);
            }
        }

        public Double getPriceAsDouble() throws IllegalArgumentException {
            try {
                return Double.parseDouble(price);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid price: " + price);
            }
        }

        public Integer getOdometerAsInt() throws IllegalArgumentException {
            try {
                return Integer.parseInt(odometer);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid odometer: " + odometer);
            }
        }
    }
}