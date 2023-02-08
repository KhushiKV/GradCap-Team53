import React from 'react'

export default function Dashboard() {
    return (
        <div className="Dashboard p-4">
            <div className="h1 text-center">MoveSafe Admin Panel</div>
            <div className="h4 text-center text-secondary">Dashboard</div>

            <div className="container-fluid p-4">
                <div className="row">
                    <div className="col-2 card p-3">
                        <div className="ul gap-3">
                            <li>
                                Dashboard
                            </li>
                            <li>
                                Users
                            </li>
                            <li>
                                Devices
                            </li>
                            <li>
                                Reports
                            </li>
                            <li>
                                Settings
                            </li>
                        </div>
                    </div>
                    <div className="col-8">

                    </div>
                </div>
            </div>

        </div>
    )
}
