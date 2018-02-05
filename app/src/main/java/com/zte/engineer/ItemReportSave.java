package com.zte.engineer;



import android.os.Parcel;
import android.os.Parcelable;

/**
*This is a class just for 
*save the state of the report items.
*
*
*
*/



public class ItemReportSave implements Parcelable {

        String title;
        String result;

        public ItemReportSave(String title, String result) {
            this.title = title;
            this.result = result;
        }

        private ItemReportSave(Parcel in) {
            title = in.readString();
            result = in.readString();
        }

        public final Creator<ItemReportSave> CREATOR = new Creator<ItemReportSave>() {
            @Override
            public ItemReportSave createFromParcel(Parcel in) {
                return new ItemReportSave(in);
            }

            @Override
            public ItemReportSave[] newArray(int size) {
                return new ItemReportSave[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(result);
        }
    }