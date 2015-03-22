package com.pairoo.domain;

import com.pairoo.domain.enums.Starsign;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Ralf Eichinger
 */
public class PersonProfile {

    /**
     * Calculates the age from the given birthdate.
     *
     * @param birthdate given birthdate
     * @return age in years
     */
    public static int getAge(final Date birthdate) {
        if (birthdate == null) {
            return -1;
        }
        final Calendar today = Calendar.getInstance();
        final Calendar birthday = Calendar.getInstance();
        birthday.setTime(birthdate);

        // Get age based on year
        int age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);

        // Add the tentative age to the date of birth to get this year's
        // birthday
        birthday.add(Calendar.YEAR, age);

        // If this year's birthday has not happened yet, subtract one from age
        if (today.before(birthday)) {
            age--;
        }
        return age;
    }

    /**
     * Gets the birthdate for a given age. Day and month of birthdate are the
     * same than today's date. Person reaches age today. It is the youngest
     * person having the given age.
     *
     * @param age age (in years)
     * @return birthdate
     */
    public static Date getMinBirthdate(final int age) {
        final Calendar birthdate = Calendar.getInstance();
        birthdate.add(Calendar.YEAR, (-1) * age);
        birthdate.set(Calendar.HOUR_OF_DAY, 23);
        birthdate.set(Calendar.MINUTE, 59);
        birthdate.set(Calendar.SECOND, 59);
        return birthdate.getTime();
    }

    /**
     * Gets the birthdate for a given age. Person reaches age + 1 tomorrow. It
     * is the oldest person having the given age.
     *
     * @param age age (in years)
     * @return birthdate
     */
    public static Date getMaxBirthdate(final int age) {
        final Calendar birthdate = Calendar.getInstance();
        birthdate.add(Calendar.YEAR, (-1) * (age + 1));
        birthdate.add(Calendar.DAY_OF_YEAR, 1);
        birthdate.set(Calendar.HOUR_OF_DAY, 0);
        birthdate.set(Calendar.MINUTE, 0);
        birthdate.set(Calendar.SECOND, 0);
        return birthdate.getTime();
    }

    /**
     * Calculates the star sign from the given birthdate.
     *
     * @param birthdate given birthdate
     * @return star sign
     */
    public static Starsign getStarsign(final Date birthdate) {
        if (birthdate == null) {
            return null;
        }
        Starsign astrologicalSign = null;

        final Calendar calBirthdate = Calendar.getInstance();
        calBirthdate.setTime(birthdate);
        final int year = calBirthdate.get(Calendar.YEAR);

        final Calendar check = Calendar.getInstance();

        // check birthdate by comparing to (end date) ascending starsigns over
        // the whole year
        while (true) {
            check.set(year, Starsign.CAPRICORNUS.getEndMonth() - 1, Starsign.CAPRICORNUS.getEndDayofMonth());
            final Date date = check.getTime();
            if (calBirthdate.compareTo(check) <= 0) {
                astrologicalSign = Starsign.CAPRICORNUS;
                break;
            }
            check.set(year, Starsign.AQUARIUS.getEndMonth() - 1, Starsign.AQUARIUS.getEndDayofMonth());
            if (calBirthdate.compareTo(check) <= 0) {
                astrologicalSign = Starsign.AQUARIUS;
                break;
            }
            check.set(year, Starsign.PISCES.getEndMonth() - 1, Starsign.PISCES.getEndDayofMonth());
            if (calBirthdate.compareTo(check) <= 0) {
                astrologicalSign = Starsign.PISCES;
                break;
            }
            check.set(year, Starsign.ARIES.getEndMonth() - 1, Starsign.ARIES.getEndDayofMonth());
            if (calBirthdate.compareTo(check) <= 0) {
                astrologicalSign = Starsign.ARIES;
                break;
            }
            check.set(year, Starsign.TAURUS.getEndMonth() - 1, Starsign.TAURUS.getEndDayofMonth());
            if (calBirthdate.compareTo(check) <= 0) {
                astrologicalSign = Starsign.TAURUS;
                break;
            }
            check.set(year, Starsign.GEMINI.getEndMonth() - 1, Starsign.GEMINI.getEndDayofMonth());
            if (calBirthdate.compareTo(check) <= 0) {
                astrologicalSign = Starsign.GEMINI;
                break;
            }
            check.set(year, Starsign.CANCER.getEndMonth() - 1, Starsign.CANCER.getEndDayofMonth());
            if (calBirthdate.compareTo(check) <= 0) {
                astrologicalSign = Starsign.CANCER;
                break;
            }
            check.set(year, Starsign.LEO.getEndMonth() - 1, Starsign.LEO.getEndDayofMonth());
            if (calBirthdate.compareTo(check) <= 0) {
                astrologicalSign = Starsign.LEO;
                break;
            }
            check.set(year, Starsign.VIRGO.getEndMonth() - 1, Starsign.VIRGO.getEndDayofMonth());
            if (calBirthdate.compareTo(check) <= 0) {
                astrologicalSign = Starsign.VIRGO;
                break;
            }
            check.set(year, Starsign.LIBRA.getEndMonth() - 1, Starsign.LIBRA.getEndDayofMonth());
            if (calBirthdate.compareTo(check) <= 0) {
                astrologicalSign = Starsign.LIBRA;
                break;
            }
            check.set(year, Starsign.SCORPIO.getEndMonth() - 1, Starsign.SCORPIO.getEndDayofMonth());
            if (calBirthdate.compareTo(check) <= 0) {
                astrologicalSign = Starsign.SCORPIO;
                break;
            }
            check.set(year, Starsign.SAGITTARIUS.getEndMonth() - 1, Starsign.SAGITTARIUS.getEndDayofMonth());
            if (calBirthdate.compareTo(check) <= 0) {
                astrologicalSign = Starsign.SAGITTARIUS;
                break;
            }

            // at the end of the year it is capricorn again...
            astrologicalSign = Starsign.CAPRICORNUS;
            break;
        }
        return astrologicalSign;
    }
}
