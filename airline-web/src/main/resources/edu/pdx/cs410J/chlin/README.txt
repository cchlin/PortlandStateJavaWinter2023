To use the program, enter the following will create a airline
and a flight, and add the flight with its information into the
airline's flight list.

java -jar target/airline-2023.0.0.jar [options] <args>
  args are (in this order):
    airline          Airline name
    flightNumber     The flight number, digits  only
    src              Three-letter departure airport code 
    depart           Departure date and time (MM/DD/YYYY hh:mm am/pm)
    dest             Three-letter destination airport code
    arrive           Arrival date and time (MM/DD/YYYY hh:mm am/pm)
  list of options:
    -host hostname               Host computer on which the server runs
    -port port                   Port on which the server is listening
    -search airline [src dest]   search for flights
    -print                       Prints the description of the new flight
    -README                      Prints this README file

Cheng Lin
chlin@pdx.edu