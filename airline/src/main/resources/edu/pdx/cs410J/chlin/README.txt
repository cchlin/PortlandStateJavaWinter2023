To use the program, enter the following will create a airline
and a flight, and add the flight with its information into the
airline's flight list.

java -jar target/airline-2023.0.0.jar [options] <args>
  args are (in this order):
    airline          Airline name
    flightNumber     The flight number, digits  only
    src              Three-letter departure airport code 
    depart           Departure date and time (MM/DD/YYYY hh:mm)
    dest             Three-letter destination airport code
    arrive           Arrival date and time (MM/DD/YYYY hh:mm)
  list of options:
    -print           Prints the description of the new flight
    -README          Prints this README file
    -textFile file   Read/write the airline info to the file

Cheng Lin
chlin@pdx.edu