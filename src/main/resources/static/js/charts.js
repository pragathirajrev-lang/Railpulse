/**
 * Railway Analytics & Demand Forecasting - Interactive Chart.js Initializer
 * High-Definition, Ultra-Large Presentation Scale Rendering
 */

// Configure Global High-DPI & Razor-Sharp Typography Defaults
if (typeof Chart !== 'undefined') {
    // Ensures crisp 2.5x rendering on Windows 125%/150% scaling and Retina monitors
    Chart.defaults.devicePixelRatio = Math.max(window.devicePixelRatio || 1, 2.5);
    Chart.defaults.font.family = "'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif";
    Chart.defaults.font.size = 17;
    Chart.defaults.font.weight = '700';
    Chart.defaults.color = '#0a0f1d';
    Chart.defaults.plugins.tooltip.titleFont = { size: 19, weight: '800' };
    Chart.defaults.plugins.tooltip.bodyFont = { size: 17, weight: '600' };
    Chart.defaults.plugins.tooltip.padding = 16;
    Chart.defaults.plugins.tooltip.cornerRadius = 12;
}

const Charts = {
    instances: {},

    colors: {
        blue: '#2563eb',
        blueDark: '#1d4ed8',
        blueLight: 'rgba(37, 99, 235, 0.2)',
        emerald: '#059669',
        emeraldLight: 'rgba(5, 150, 105, 0.2)',
        amber: '#d97706',
        rose: '#dc2626',
        purple: '#7c3aed',
        slate: '#0f172a',
        slateMuted: '#334155',
        grid: '#cbd5e1'
    },

    destroyChart(key) {
        if (this.instances[key]) {
            this.instances[key].destroy();
            delete this.instances[key];
        }
    },

    // 1. Dashboard: Overall Passenger Traffic Trend Line Chart
    renderTrafficTrend(canvasId, trendData) {
        this.destroyChart('trafficTrend');
        const ctx = document.getElementById(canvasId);
        if (!ctx) return;

        const labels = trendData.map(d => d.date);
        const data = trendData.map(d => d.passengers);

        this.instances['trafficTrend'] = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Daily Passengers Across Network',
                    data: data,
                    borderColor: this.colors.blue,
                    backgroundColor: this.colors.blueLight,
                    borderWidth: 4,
                    fill: true,
                    tension: 0.35,
                    pointRadius: 4,
                    pointHoverRadius: 8
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: function(ctx) {
                                return ` Passengers: ${Number(ctx.raw).toLocaleString()} riders`;
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        grid: { display: false },
                        ticks: { maxTicksLimit: 12, color: this.colors.slate, font: { size: 16, weight: '700' } }
                    },
                    y: {
                        grid: { color: this.colors.grid },
                        ticks: {
                            color: this.colors.slate,
                            font: { size: 16, weight: '700' },
                            callback: val => Number(val).toLocaleString()
                        }
                    }
                }
            }
        });
    },

    // 2. Dashboard: Route-Wise Passenger Comparison Horizontal Bar
    renderRouteComparison(canvasId, routes) {
        this.destroyChart('routeComparison');
        const ctx = document.getElementById(canvasId);
        if (!ctx) return;

        // Top 7 routes
        const topRoutes = routes.slice(0, 7);
        const labels = topRoutes.map(r => r.route);
        const data = topRoutes.map(r => r.totalPassengers);

        this.instances['routeComparison'] = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Total Passengers',
                    data: data,
                    backgroundColor: [
                        '#1e40af', '#2563eb', '#3b82f6', '#60a5fa',
                        '#0284c7', '#0ea5e9', '#38bdf8'
                    ],
                    borderRadius: 8
                }]
            },
            options: {
                indexAxis: 'y',
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: ctx => ` ${Number(ctx.raw).toLocaleString()} total passengers`
                        }
                    }
                },
                scales: {
                    x: {
                        grid: { color: this.colors.grid },
                        ticks: {
                            color: this.colors.slate,
                            font: { size: 16, weight: '700' },
                            callback: val => Number(val).toLocaleString()
                        }
                    },
                    y: {
                        grid: { display: false },
                        ticks: { color: this.colors.slate, font: { size: 16, weight: '800' } }
                    }
                }
            }
        });
    },

    // 3. Dashboard: Train Occupancy Donut Chart
    renderOccupancyDonut(canvasId, occData) {
        this.destroyChart('occupancyDonut');
        const ctx = document.getElementById(canvasId);
        if (!ctx) return;

        const labels = ['Low (<50%)', 'Moderate (50-75%)', 'High (75-90%)', 'Critical (>90%)'];
        const values = [
            occData['Low'] || 0,
            occData['Moderate'] || 0,
            occData['High'] || 0,
            occData['Critical'] || 0
        ];

        this.instances['occupancyDonut'] = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: labels,
                datasets: [{
                    data: values,
                    backgroundColor: ['#38bdf8', '#10b981', '#f59e0b', '#ef4444'],
                    borderWidth: 3,
                    borderColor: '#ffffff'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: { font: { size: 17, weight: '800' }, color: this.colors.slate, padding: 22 }
                    },
                    tooltip: {
                        callbacks: {
                            label: ctx => ` ${ctx.label}: ${ctx.raw}% of trips`
                        }
                    }
                },
                cutout: '52%'
            }
        });
    },

    // 4. Monthly Passenger Distribution
    renderMonthlyDistribution(canvasId, monthMap) {
        this.destroyChart('monthlyDist');
        const ctx = document.getElementById(canvasId);
        if (!ctx) return;

        const labels = Object.keys(monthMap).map(m => m.substring(0, 3));
        const values = Object.values(monthMap);

        this.instances['monthlyDist'] = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Monthly Passenger Volume',
                    data: values,
                    backgroundColor: '#2563eb',
                    borderRadius: 8
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: ctx => ` ${Number(ctx.raw).toLocaleString()} passengers`
                        }
                    }
                },
                scales: {
                    x: {
                        grid: { display: false },
                        ticks: { color: this.colors.slate, font: { size: 16, weight: '700' } }
                    },
                    y: {
                        grid: { color: this.colors.grid },
                        ticks: {
                            color: this.colors.slate,
                            font: { size: 16, weight: '700' },
                            callback: v => Number(v).toLocaleString()
                        }
                    }
                }
            }
        });
    },

    // 5. Weekday vs Weekend Bar Chart
    renderWeekdayVsWeekend(canvasId, dowMap) {
        this.destroyChart('dowChart');
        const ctx = document.getElementById(canvasId);
        if (!ctx) return;

        const labels = Object.keys(dowMap).map(d => d.substring(0, 3));
        const values = Object.values(dowMap);
        const bgColors = Object.keys(dowMap).map(d =>
            (d === 'Friday' || d === 'Saturday' || d === 'Sunday') ? '#2563eb' : '#64748b'
        );

        this.instances['dowChart'] = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Total Passenger Traffic',
                    data: values,
                    backgroundColor: bgColors,
                    borderRadius: 8
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: ctx => ` ${Number(ctx.raw).toLocaleString()} passengers`
                        }
                    }
                },
                scales: {
                    x: {
                        grid: { display: false },
                        ticks: { color: this.colors.slate, font: { size: 16, weight: '700' } }
                    },
                    y: {
                        grid: { color: this.colors.grid },
                        ticks: {
                            color: this.colors.slate,
                            font: { size: 16, weight: '700' },
                            callback: v => Number(v).toLocaleString()
                        }
                    }
                }
            }
        });
    },

    // 6. Delay Severity Distribution
    renderDelayDistribution(canvasId, delayMap) {
        this.destroyChart('delayDist');
        const ctx = document.getElementById(canvasId);
        if (!ctx) return;

        const labels = Object.keys(delayMap);
        const values = Object.values(delayMap);

        this.instances['delayDist'] = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: labels,
                datasets: [{
                    data: values,
                    backgroundColor: ['#10b981', '#38bdf8', '#f59e0b', '#ef4444'],
                    borderWidth: 3,
                    borderColor: '#ffffff'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: { font: { size: 17, weight: '800' }, color: this.colors.slate, padding: 22 }
                    },
                    tooltip: {
                        callbacks: {
                            label: ctx => ` ${ctx.label}: ${Number(ctx.raw).toLocaleString()} trips`
                        }
                    }
                },
                cutout: '52%'
            }
        });
    },

    // 7. Spark MLlib Demand Forecasting Chart (Actual vs Test Prediction vs Next 7-Day Forecast)
    renderForecastChart(canvasId, forecastResult) {
        this.destroyChart('forecastChart');
        const ctx = document.getElementById(canvasId);
        if (!ctx) return;

        const evalPoints = forecastResult.testEvaluationPoints || [];
        const futurePoints = forecastResult.next7DaysForecast || [];

        const labels = [];
        const actualData = [];
        const testPredData = [];
        const futurePredData = [];

        // Add test evaluation points
        evalPoints.forEach(p => {
            labels.push(p.date);
            actualData.push(p.actualValue);
            testPredData.push(p.predictedValue);
            futurePredData.push(null); // no future prediction on test points
        });

        // Add bridge point from last evaluation point to first future point for continuity
        if (evalPoints.length > 0 && futurePoints.length > 0) {
            futurePredData[futurePredData.length - 1] = testPredData[testPredData.length - 1];
        }

        // Add future points
        futurePoints.forEach(p => {
            labels.push(p.date + ' (Forecast)');
            actualData.push(null);
            testPredData.push(null);
            futurePredData.push(p.predictedValue);
        });

        this.instances['forecastChart'] = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: 'Actual Historical Passenger Demand',
                        data: actualData,
                        borderColor: '#2563eb',
                        backgroundColor: '#2563eb',
                        borderWidth: 4,
                        pointRadius: 5,
                        pointHoverRadius: 9,
                        tension: 0.2
                    },
                    {
                        label: 'Spark MLlib Test Set Prediction',
                        data: testPredData,
                        borderColor: '#7c3aed',
                        backgroundColor: '#7c3aed',
                        borderWidth: 3.5,
                        borderDash: [6, 6],
                        pointRadius: 5,
                        pointHoverRadius: 8,
                        tension: 0.2
                    },
                    {
                        label: 'Next 7-Day Future Passenger Forecast',
                        data: futurePredData,
                        borderColor: '#059669',
                        backgroundColor: 'rgba(5, 150, 105, 0.2)',
                        borderWidth: 4.5,
                        pointRadius: 6,
                        pointHoverRadius: 10,
                        pointBackgroundColor: '#059669',
                        fill: false,
                        tension: 0.25
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                interaction: {
                    mode: 'index',
                    intersect: false
                },
                plugins: {
                    legend: {
                        position: 'top',
                        labels: { font: { size: 17, weight: '800' }, color: this.colors.slate, padding: 22 }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(ctx) {
                                if (ctx.raw === null) return null;
                                return ` ${ctx.dataset.label}: ${Number(ctx.raw).toLocaleString()} passengers`;
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        grid: { display: false },
                        ticks: { color: this.colors.slate, font: { size: 16, weight: '700' } }
                    },
                    y: {
                        grid: { color: this.colors.grid },
                        ticks: {
                            color: this.colors.slate,
                            font: { size: 16, weight: '700' },
                            callback: val => Number(val).toLocaleString()
                        }
                    }
                }
            }
        });
    }
};
