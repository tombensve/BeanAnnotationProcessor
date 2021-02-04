package se.natusoft.annotation.cobolrecordbeanannotationprocessor.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// This is an example of what the processor needs to generate. T
// NOT USED SAMPLE ONLY
public class CobolRecodBeanToGenerate {

    public static class CRBDate {
        private String date;
        private SimpleDateFormat sdf;

        public CRBDate( String date ) {
            this( date, null );
        }

        public CRBDate( String date, String dateFormat ) {
            this.date = date;
            if ( dateFormat != null && !( dateFormat.length() == 0 ) ) {
                this.sdf = new SimpleDateFormat( dateFormat );
            }
        }

        public CRBDate( Date date, String dateFormat ) {
            if ( dateFormat == null ) throw new IllegalArgumentException( "Must supply date format!" );

            this.sdf = new SimpleDateFormat( dateFormat );
            this.date = this.sdf.format( date );
        }

        public Date getDate() {
            Date date = null;
            if ( this.sdf != null ) {
                try {
                    date = this.sdf.parse( this.date );
                } catch ( ParseException pe ) {
                    throw new IllegalArgumentException( "Date format and actual date string does not match!", pe );
                }
            }
            return date;
        }

        public void setDate( Date date ) {
            if ( this.sdf == null ) {
                throw new IllegalArgumentException( "No date format has been provided!" );
            }
            this.date = this.sdf.format( date );
        }

        public void setDate( String date ) {
            this.date = date;
        }

        public String toString() {
            return this.date;
        }
    }

    private static String ensureSize( String field, int size ) {
        if ( field.length() > size ) {
            field = field.substring( 0, size - 1 );
        } else while ( field.length() < size ) {
            field = field + " ";
        }
        return field;
    }

    public CobolRecodBeanToGenerate() {

    }


    public CobolRecodBeanToGenerate( String record ) {
        this.name = record.substring( 0, 6 );
        this.date = new CRBDate( record.substring( 5, 6 ), null );
        // ...
    }

    private String name;
    public CobolRecodBeanToGenerate setName( String name ) {
        this.name = ensureSize( name, 10 );
        return this;
    }

    public String getName() {
        return this.name;
    }

    private CRBDate date;

    public CobolRecodBeanToGenerate setDate( CobolRecodBeanToGenerate.CRBDate date ) {
        this.date = date;
        return this;
    }

    public CobolRecodBeanToGenerate setDate( String date ) {
        setDate( new CobolRecodBeanToGenerate.CRBDate( date ) );
        return this;
    }

    public CobolRecodBeanToGenerate.CRBDate getDate() {
        return this.date;
    }

    private String count;

    public CobolRecodBeanToGenerate setCount( String count ) {
        this.count = ensureSize( count, 10 );
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( this.name );
        sb.append( this.date.toString() );
        sb.append( this.count );

        return sb.toString();
    }
}
