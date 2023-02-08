import axios from 'axios'
import React from 'react'
import { useNavigate } from 'react-router-dom'

export default function LoginPage({ setAuth, auth, loading, setLoading }) {

    const [email, setEmail] = React.useState('')
    const [password, setPassword] = React.useState('')
    const navigate = useNavigate()

    React.useEffect(() => {
        if (auth)
            navigate('/')
    }, [auth, navigate, loading, setLoading])

    const loginUser = (e) => {
        e.preventDefault()
        axios.post(`${process.env.REACT_APP_API_URL}/auth/login`, {
            email,
            password
        }, {
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.data.user.role === 'admin') {
                localStorage.setItem('token', res.data.token)
                console.log(res.data.message);
                setAuth(true)
                navigate('/')
            } else {
                alert('You are not an admin')
            }

        }).catch(err => {
            console.log(err.response.data)
        })
    }

    return (
        <div className=''>

            <br />
            <br />

            {
                loading ? <div className="text-center mt-3">
                    <div className="spinner-border text-primary me-2" role="status">
                    </div>
                    <span className="sr-only text-primary fs-3 fw-bold">Loading...</span>
                </div> : <div style={{ width: 400, margin: 'auto' }}
                    className='d-flex flex-column justify-content-center align-items-center card p-5'>
                    <div className="h2">Login</div>
                    <form onSubmit={loginUser}>
                        <div className="form-group ">
                            <label for="exampleInputEmail1">Email address</label>
                            <input type="email" onChange={(e) => setEmail(e.target.value)} className="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email" />
                            <small id="emailHelp" className="form-text text-muted">
                                We'll never share your email with anyone else.
                            </small>
                            <br />
                            <br />
                            <label for="exampleInputPassword1">Password</label>
                            <input type="password" onChange={(e) => setPassword(e.target.value)} className="form-control" id="exampleInputPassword1" placeholder="Password" />
                            <br />
                        </div>
                        <button type="submit" className="btn btn-primary w-100">Login</button>
                    </form>
                </div>

            }



        </div>
    )
}
