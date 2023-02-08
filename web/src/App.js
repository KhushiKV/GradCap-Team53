import { useEffect, useState } from "react";
import { Route, Routes, useNavigate } from "react-router-dom";
import axios from 'axios';
import React from "react";
import DashboardPage from "./DashboardPage";
import LoginPage from "./LoginPage";


function App() {

    const [auth, setAuth] = useState(false);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();


    useEffect(() => {
        let token = localStorage.getItem('token')
        setLoading(true)
        if (token) {
            axios.get(`${process.env.REACT_APP_API_URL}/user`, {
                headers: {
                    'Authorization': token
                }
            })
                .then(res => {
                    console.log(res.data);
                    if (res.data.role === 'admin') {
                        setAuth(true)
                        setLoading(false)
                    } else {
                        setAuth(false)
                        navigate('/login')
                        setLoading(false)
                    }
                })
                .catch(err => {
                    console.log(err);
                    setAuth(false)
                    navigate('/login')
                    setLoading(false)
                })
        } else {
            setAuth(false)
            navigate('/login')
            setLoading(false)
        }

    }, [navigate])

    return (
        <>
            <div className="App p-3">
                <div className=" text-center">
                    <img src="logo.png" alt="logo" border="0" height={70} />
                </div>
                <div className="h5 text-center text-secondary">Admin Panel</div>

                <Routes>
                    <Route path="/" element={<DashboardPage auth={auth} setAuth={setAuth} loading={loading} setLoading={setLoading} />} />
                    <Route path='/login' element={<LoginPage auth={auth} setAuth={setAuth} loading={loading} setLoading={setLoading} />} />
                    <Route path="*" element={<h1>404 Not Found</h1>} />
                </Routes>


            </div>
        </>
    );
}

export default App;
