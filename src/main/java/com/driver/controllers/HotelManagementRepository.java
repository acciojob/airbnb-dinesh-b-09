package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
public class HotelManagementRepository {

    HashMap<String , Hotel> hotelmap = new HashMap<>();
    HashMap<Integer,  User> usermap = new HashMap<>();
    HashMap<String, Booking> bookmap = new HashMap<>();
    public String addHotel(Hotel hotel)
    {
        if(hotelmap.containsKey(hotel.getHotelName()) || hotel.getHotelName() == null || hotel == null)
        {
            return "FAILURE";
        }

        hotelmap.put(hotel.getHotelName(),hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user)
    {
        usermap.put(user.getaadharCardNo(), user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities()
    {
        int max=0;
        String s= "";
        for(Hotel hotel : hotelmap.values())
        {
            if(hotel.getFacilities().size() > max)
            {
                max = hotel.getFacilities().size();
                s = hotel.getHotelName();
            }
            else if(hotel.getFacilities().size() == max)
            {
                if(hotel.getHotelName().compareTo(s) < 0)
                {
                    s = hotel.getHotelName();
                }
            }
        }
        return s;
    }

    public int bookARoom(Booking booking)
    {
        UUID uuid = UUID.randomUUID();
        String bookingId = uuid.toString();
        booking.setBookingId(bookingId);

        Hotel hotel = hotelmap.get(booking.getHotelName());

       // int price = hotel.getPricePerNight();

        if(booking.getNoOfRooms() > hotel.getAvailableRooms())
        {
            return -1;
        }

        booking.setAmountToBePaid(hotel.getPricePerNight() * booking.getNoOfRooms());

        hotel.setAvailableRooms(hotel.getAvailableRooms() - booking.getNoOfRooms());

        bookmap.put(bookingId , booking);

        hotelmap.put(hotel.getHotelName() , hotel);

        return booking.getAmountToBePaid();


    }

    public int getBookings(Integer aadharCard)
    {
        int count = 0;

        for(Booking booking : bookmap.values())
        {
            if(booking.getBookingAadharCard() == aadharCard)
            {
                count++;
            }
        }

        return count;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName)
    {
        Hotel hotel = hotelmap.get(hotelName);
        List<Facility> currentfacilities = hotel.getFacilities();

        for(Facility facility : newFacilities)
        {
            if(!currentfacilities.contains(facility))
            {
                currentfacilities.add(facility);
            }
        }
        hotel.setFacilities(currentfacilities);
        hotelmap.put(hotelName , hotel);
        return hotel;
    }
}
