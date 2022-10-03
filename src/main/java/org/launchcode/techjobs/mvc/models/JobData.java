package org.launchcode.techjobs.mvc.models;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.launchcode.techjobs.mvc.NameSorter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "job_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<Job> allJobs;
    private static ArrayList<Employer> allEmployers = new ArrayList<>();
    private static ArrayList<Location> allLocations = new ArrayList<>();
    private static ArrayList<PositionType> allPositionTypes = new ArrayList<>();
    private static ArrayList<CoreCompetency> allCoreCompetency = new ArrayList<>();


    public static ArrayList<Job> findAll() {

        loadData();

        return new ArrayList<>(allJobs);
    }


    public static ArrayList<Job> findByColumnAndValue(String column, String value) {

        loadData();

        ArrayList<Job> jobs = new ArrayList<>();

        if (value.equalsIgnoreCase("all")) {
            return findAll();
        }

        if (column.equals("all")) {
            jobs = findByValue(value);
            return jobs;
        }
        for (Job job : allJobs) {

            String aValue = getFieldValue(job, column);

            if (aValue != null && aValue.toLowerCase().contains(value.toLowerCase())) {
                jobs.add(job);
            }
        }

        return jobs;
    }

    public static String getFieldValue(Job job, String fieldName) {
        String theValue;
        if (fieldName.equals("name")) {
            theValue = job.getName();
        } else if (fieldName.equals("employer")) {
            theValue = job.getEmployer().toString();
        } else if (fieldName.equals("location")) {
            theValue = job.getLocation().toString();
        } else if (fieldName.equals("positionType")) {
            theValue = job.getPositionType().toString();
        } else {
            theValue = job.getCoreCompetency().toString();
        }

        return theValue;
    }

    public static ArrayList<Job> findByValue(String value) {

        loadData();

        ArrayList<Job> jobs = new ArrayList<>();




        for (Job job : allJobs) {

            if (job.getName().toLowerCase().contains(value.toLowerCase())) {
                jobs.add(job);
            } else if (job.getEmployer().toString().toLowerCase().contains(value.toLowerCase())) {
                jobs.add(job);
            } else if (job.getLocation().toString().toLowerCase().contains(value.toLowerCase())) {
                jobs.add(job);
            } else if (job.getPositionType().toString().toLowerCase().contains(value.toLowerCase())) {
                jobs.add(job);
            } else if (job.getCoreCompetency().toString().toLowerCase().contains(value.toLowerCase())) {
                jobs.add(job);
            }


        }

        return jobs;
    }

    private static Object findExistingObject(ArrayList list, String value) {
        for (Object item : list) {
            if (item.toString().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }


    private static void loadData() {

        if (isDataLoaded) {
            return;
        }

        try {


            Resource resource = new ClassPathResource(DATA_FILE);
            InputStream is = resource.getInputStream();
            Reader reader = new InputStreamReader(is);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();


            for (CSVRecord record : records) {

                String aName = record.get(0);
                String anEmployer = record.get(1);
                String aLocation = record.get(2);
                String aPosition = record.get(3);
                String aSkill = record.get(4);

                Employer newEmployer = (Employer) findExistingObject(allEmployers, anEmployer);
                Location newLocation = (Location) findExistingObject(allLocations, aLocation);
                PositionType newPosition = (PositionType) findExistingObject(allPositionTypes, aPosition);
                CoreCompetency newSkill = (CoreCompetency) findExistingObject(allCoreCompetency, aSkill);

                if (newEmployer == null) {
                    newEmployer = new Employer(anEmployer);
                    allEmployers.add(newEmployer);
                }

                if (newLocation == null) {
                    newLocation = new Location(aLocation);
                    allLocations.add(newLocation);
                }

                if (newSkill == null) {
                    newSkill = new CoreCompetency(aSkill);
                    allCoreCompetency.add(newSkill);
                }

                if (newPosition == null) {
                    newPosition = new PositionType(aPosition);
                    allPositionTypes.add(newPosition);
                }

                Job newJob = new Job(aName, newEmployer, newLocation, newPosition, newSkill);

                allJobs.add(newJob);
            }

            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

    public static ArrayList<Employer> getAllEmployers() {
        loadData();
        allEmployers.sort(new NameSorter());
        return allEmployers;
    }

    public static ArrayList<Location> getAllLocations() {
        loadData();
        allLocations.sort(new NameSorter());
        return allLocations;
    }

    public static ArrayList<PositionType> getAllPositionTypes() {
        loadData();
        allPositionTypes.sort(new NameSorter());
        return allPositionTypes;
    }

    public static ArrayList<CoreCompetency> getAllCoreCompetency() {
        loadData();
        allCoreCompetency.sort(new NameSorter());
        return allCoreCompetency;
    }

}