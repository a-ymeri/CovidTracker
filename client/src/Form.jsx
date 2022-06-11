import React, { useEffect, useState } from "react";
import axios from 'axios';
import Grid from "react-fast-grid";
import DatePicker from 'react-datepicker';
import { Line } from 'react-chartjs-2';
import "react-datepicker/dist/react-datepicker.css";
import 'bootstrap/dist/css/bootstrap.min.css';
import ThreeFieldForm from "./ThreeFieldForm";

function Form() {
    const [startingDate, setStartingDate] = useState()
    const [endingDate, setEndingDate] = useState();
    const [deleteDate, setDeleteDate] = useState(new Date(Date.now()));
    const dateOptions = {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
    };

    const [median, setMedian] = useState({
        covidAdmissions: 0,
        covidCases: 0,
        covidDeaths: 0
    });
    const [mean, setMean] = useState({
        covidAdmissions: 0,
        covidCases: 0,
        covidDeaths: 0
    });

    const [covidCases, setCovidCases] = useState([1, 2, 3, 4])
    const [covidAdmissions, setCovidAdmissions] = useState([1, 2, 3, 4])
    const [covidDeaths, setCovidDeaths] = useState([1, 2, 3, 4])

    const [dates, setDates] = useState([])
    const data = {
        labels: dates,
        datasets: [
            {
                label: '# of Covid-19 cases',
                data: covidCases,
                fill: false,
                backgroundColor: 'rgb(255, 99, 132)',
                borderColor: 'rgba(255, 99, 132, 0.2)',
            },
        ]
    };

    const data1 = {
        labels: dates,
        datasets: [{
            label: '# of Covid-19 admissions',
            data: covidAdmissions,
            fill: false,
            backgroundColor: 'rgb(2, 99, 12)',
            borderColor: 'rgba(2, 99, 13, 0.2)',
        }]
    }
    const data2 = {
        labels: dates,
        datasets: [{

            label: '# of Covid-19 deaths',
            data: covidDeaths,
            fill: false,
            backgroundColor: 'rgb(0, 12, 200)',
            borderColor: 'rgba(0, 12, 200, 0.2)',
        }]
    };

    const options = {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero: true
                }
            }]
        }
    }




    const baseUrl = "https://covid-tracker-cw.herokuapp.com/api";

    function createData(body) {

        axios.post(baseUrl, body).then((response) => {
            console.log(response)
            if (response.status == 201)
                alert("Entry created successfully at date: " + body.date);

        }).catch((error) => {
            if (error.status == 401 || error.status == 403)
                alert("That command can only be used by admins!")
            else
                alert("An entry for date " + body.date + " already exists");

        });

    }

    function updateData(body) {

        axios.put(baseUrl + "/" + body.date, body).then((response) => {
            console.log(response)
            if (response.status == 200)
                alert("Entry update successfully for date: " + body.date);
            else {
                alert("Entry for date " + body.date + " did not exist. New entry added for date " + body.date)
            }
        }
        ).catch((error) => {
            if (error.status == 401 || error.status == 403)
                alert("That command can only be used by admins!")
            else
                alert("Entry for date " + body.date + " does not exist")
        }
        );

    }
    function deleteData(body) {

        body = body.toLocaleDateString("en-gb", dateOptions).replaceAll("/", "-")
        axios.delete(baseUrl + "/" + body).then((response) => {
            alert("Entry for date " + body + " successfully deleted");
        }).catch((error) => {
            if (error.status == 401 || error.status == 403)
                alert("That command can only be used by admins!")
            else
                alert("An entry for date " + body + " does not exist!")
        });

    }
    // const baseUrl = "https://covid-tracker-cw.herokuapp.com/api";

    function submitClicked(event) {

        event.preventDefault();
        if (document.getElementById("allData").checked) getAllData();
        if (document.getElementById("mean").checked) getMean();
        if (document.getElementById("median").checked) getMedian();
    }

    function getAllData() {


        if (startingDate && endingDate) {

            // axios({
            //     headers: { "Content-Type": "application/x-www-form-urlencoded" , 'Authorization': "Basic c3ByaW5nYWRtaW46YWRtaW4xMjM=" },
            //     method: "get",
            //     url: baseUrl,
            //     auth: {
            //       username: username,
            //       password: password
            //     }
            //   }).then(console.log("hey?")).catch(()=>{alert("hge")});

            axios.get(baseUrl, {
                params: {
                    startDate: startingDate.toLocaleDateString("en-gb", dateOptions).replaceAll("/", "-"),
                    endDate: endingDate.toLocaleDateString("en-gb", dateOptions).replaceAll("/", "-")
                }
                // headers: { 'Authorization': "Basic c3ByaW5nYWRtaW46YWRtaW4xMjM=" }
            }
            ).then((response) => {
                console.log("hey?")
                changeCharts(response.data)
            })
        } else {
            axios.get(baseUrl).then((response) => {
                console.log(response)
                changeCharts(response.data)
            })
        }


    }

    function changeCharts(data) {
        console.log(data)
        let dates = [];
        let cases = [];
        let admissions = [];
        let deaths = [];
        data.forEach((el) => {
            dates.push(el.date);
            cases.push(el.covidCases);
            admissions.push(el.covidAdmissions);
            deaths.push(el.covidDeaths);
        })
        setDates(dates);
        setCovidCases(cases);
        setCovidAdmissions(admissions);
        setCovidDeaths(deaths);
    }

    function getMean() {
        if (startingDate && endingDate) {
            axios.get(baseUrl + "/mean", {
                params: {
                    startDate: startingDate.toLocaleDateString("en-gb", dateOptions).replaceAll("/", "-"),
                    endDate: endingDate.toLocaleDateString("en-gb", dateOptions).replaceAll("/", "-")
                },
            }).then((response) => {
                setMean(response.data);
            })
        } else {
            axios.get(baseUrl + "/mean").then((response) => {
                setMean(response.data);
            })
        }
    }

    function getMedian() {

        if (startingDate && endingDate) {
            axios.get(baseUrl + "/median", {
                params: {
                    startDate: startingDate.toLocaleDateString("en-gb", dateOptions).replaceAll("/", "-"),
                    endDate: endingDate.toLocaleDateString("en-gb", dateOptions).replaceAll("/", "-")

                }
            }).then((response) => {
                setMedian(response.data);
            })
        } else {
            axios.get(baseUrl + "/median").then((response) => {
                setMedian(response.data);
            })
        }
    }

    return (
        <div className="grid-container">
            <div className="grid-item grid-item-1"
            // style={{ display: "flex", alignItems: "center", justifyContent: "center", background: "#b3e5fc" }} className={"col"}
            >
                <div style={{ display: "flex", flexDirection: "column", paddingRight: "50px", marginRight: "50px", borderRight: "#000 solid" }}>
                    <h1>Read data</h1>
                    <span style={{ display: "block-inline" }}>Starting date </span>
                    <DatePicker
                        selected={startingDate}
                        onChange={(date) => setStartingDate(date)}
                        name="startDate"
                        dateFormat="dd/MM/yyyy"
                        autoComplete="off"

                    />

                    <span style={{ display: "block-inline" }}>Ending date</span>
                    <DatePicker
                        selected={endingDate}
                        onChange={(date) => setEndingDate(date)}
                        name="startDate"
                        dateFormat="dd/MM/yyyy"
                        autoComplete="off"
                    />

                    <div style={{ display: "flex", flexDirection: "column" }}>
                        <div>
                            <input type="checkbox" id="allData" value="allData" />
                            <label for="allData" style={{ paddingLeft: "10px" }}>All data</label>
                        </div>
                        <div>
                            <input type="checkbox" id="mean" />
                            <label for="mean" style={{ paddingLeft: "10px" }}>Mean</label>
                        </div>
                        <div>
                            <input type="checkbox" id="median" />
                            <label for="median" style={{ paddingLeft: "10px" }}>Median</label>
                        </div>
                    </div>
                    <br></br>
                    <button form="login" onClick={submitClicked} className="btn" style={{ background: "#90ee90" }} > Submit</button>
                </div>
                <ThreeFieldForm id={1} text={"Create data"} submitFunction={createData} ></ThreeFieldForm>
                <ThreeFieldForm id={2} text={"Update data"} submitFunction={updateData}></ThreeFieldForm>

                <div style={{ display: "flex", flexDirection: "column", paddingRight: "50px", marginRight: "50px", borderRight: "#000 solid" }}>
                    <h1>Delete data</h1>
                    <span style={{ display: "block-inline" }}>Date </span>
                    <DatePicker
                        selected={deleteDate}
                        onChange={(date) => setDeleteDate(date)}
                        name="startDate"
                        dateFormat="dd/MM/yyyy"
                        autoComplete="off"

                    />
                    <br></br>
                    <button form="login" onClick={() => { deleteData(deleteDate) }} className="btn" style={{ background: "#90ee90" }} > Submit</button>

                </div>

                <a href="/logout"> <button>
                    LOG OUT
                </button></a>

                {/* <div style={{ display: "flex", flexDirection: "column" }}>
                    <form id="login" onSubmit={(e) => e.preventDefault()}>
                        <h5>Write the username and password in order to send a request</h5>
                        <span>Username</span>
                        <input type="text" id="username" required />
                        <br></br>
                        <span>Password</span>
                        <input type="password" id="password" required />
                    </form>
                </div> */}

            </div>




            <div className="grid-item grid-item-3">

                <div className="grid-item median">

                    <h1>Median:</h1>
                    <p>Covid admissions: <b>{median.covidAdmissions}</b></p>
                    <p>Covid cases: <b>{median.covidCases}</b></p>
                    <p>Covid deaths: <b>{median.covidDeaths}</b></p>
                </div>
                <div className="grid-item mean">
                    <h1>Mean:</h1>
                    <p>Covid admissions: <b>{mean.covidAdmissions}</b></p>
                    <p>Covid cases: <b>{mean.covidCases}</b></p>
                    <p>Covid deaths: <b>{mean.covidDeaths}</b></p>
                </div>
            </div>





            <div className="grid-item grid-item-2 cases">
                <div className='header'>
                    <h4 className='title'>Covid Cases</h4>
                </div>
                <Line data={data} options={options} />
            </div>

            <div className="grid-item grid-item-2 admissions">
                <div className='header'>
                    <h4 className='title'>Covid Admissions</h4>
                </div>
                <Line data={data1} options={options} />
            </div>

            <div className="grid-item grid-item-2 deaths">
                <div className='header'>
                    <h4 className='title'>Covid Deaths</h4>
                </div>
                <Line data={data2} options={options} />
            </div>





        </div>
    )
}
export default Form;
