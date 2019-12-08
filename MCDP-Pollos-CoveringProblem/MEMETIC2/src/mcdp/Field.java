/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcdp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author francisco
 */
public class Field implements Comparable<Field> {

    private ArrayList<Individual> individuals;
    private int numberOfIndividuals;
    private final String idString;

    public Field() {
        this.idString = randomString(30);
        this.individuals = new ArrayList<Individual>();
        this.numberOfIndividuals = 0;
    }

    public String randomString(int length) {
        Random r = new Random(); // perhaps make it a class variable so you don't make a new one every time
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) (r.nextInt((int) (1000)));
            sb.append(c);
        }
        return sb.toString();
    }

    public void sortIndividualByFitness() {
        Collections.sort(this.individuals);
    }

    public void toConsoleIndividualsFitness() {
        for (int i = 0; i < this.individuals.size(); i++) {
            System.out.println("Fitness : " + this.individuals.get(i).getFitness());
        }
    }

    public int getNumberOfIndividuals() {
        return numberOfIndividuals;
    }

    public void setNumberOfIndividuals(int numberOfIndividuals) {
        this.numberOfIndividuals = numberOfIndividuals;
    }

    public void addNumberOfIndividuals() {
        this.numberOfIndividuals++;
    }

    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(ArrayList<Individual> individuals) {
        this.individuals = individuals;
    }

    public String getIdString() {
        return idString;
    }

    @Override
    public int compareTo(Field o) {

        /*
        The firs element of the individuals array will be the expert individual        
         */
        //return o.getIndividuals().get(0).getFitness() - this.getIndividuals().get(0).getFitness();
        return this.getIndividuals().get(0).getFitness() - o.getIndividuals().get(0).getFitness();

    }

    public int getSumFitness() {

        int sumFitness = 0;
        for (int i = 0; i < this.getIndividuals().size(); i++) {
            sumFitness += this.getIndividuals().get(i).getFitness();
        }
        return sumFitness;
    }

    public Individual selectIndividualForChange(int sumFitness) {

        Individual individualForChange = new Individual();
        double random = (new Random().nextDouble()) * sumFitness;

        /*
        Roulette wheel selection
         */
        for (int i = 0; i < this.getIndividuals().size(); i++) {
            random -= this.getIndividuals().get(i).getFitness();
            if (random <= 0) {
                individualForChange = this.getIndividuals().get(i);

                /*
                Removed of field
                 */
                this.getIndividuals().remove(i);
                this.numberOfIndividuals--;
                break;
            }

        }
        return individualForChange;
    }

    public float getfieldRankProbability(ArrayList<Field> copyField, int fieldsSize) {

        float fieldRankProbability = 0;
        int i = 0;
        for (i = 0; i < copyField.size(); i++) {
            if (this.idString.equals(copyField.get(i).getIdString())) {
                fieldRankProbability = (float) i / (fieldsSize + 1);
                break;
            }
        }
        return fieldRankProbability;

    }

}
