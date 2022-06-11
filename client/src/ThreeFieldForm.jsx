import React, { useEffect, useState } from "react";

import DatePicker from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";
import 'bootstrap/dist/css/bootstrap.min.css';

function ThreeFieldForm({ id, text, submitFunction }) {

    const dateOptions = {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
    };


    const datePatch = new Date(Date.now());
    const [date, setDate] = useState(datePatch);
    return (
        <div style={{ display: "flex", flexDirection: "column", paddingRight: "50px", marginRight: "50px", borderRight: "#000 solid" }}>
            <h1>{text}</h1>
            <span style={{ display: "block-inline" }}>Date </span>
            <DatePicker
                selected={date}
                onChange={(date) => setDate(date)}
                name="startDate"
                dateFormat="dd/MM/yyyy"
                autoComplete="off"

            />

            <span>Covid cases</span>
            <input type="text" id={"cases" + id} />
            <span>Covid admissions</span>
            <input type="text" id={"admissions" + id} />
            <span>Covid deaths</span>
            <input type="text" id={"deaths" + id} /> <br></br>
            <button onClick={(e) => {

                e.preventDefault();
                if (date) {

                    let data = {
                        covidCases: document.getElementById("cases" + id).value,
                        covidAdmissions: document.getElementById("admissions" + id).value,
                        covidDeaths: document.getElementById("deaths" + id).value,
                        date: date.toLocaleDateString("en-gb", dateOptions).replaceAll("/", "-"),
                    }
                    if (data.covidCases && data.covidDeaths && data.covidAdmissions) {
                        submitFunction(data)
                    }
                    else {
                        alert("Please put in all the data before submitting!")
                    }
                } else {
                    alert("Write in the date before submitting")
                }
            }} className="btn" style={{ background: "#90ee90" }} > Submit</button>
        </div>
    )
}

export default ThreeFieldForm;