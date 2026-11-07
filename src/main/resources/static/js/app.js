/**
 * Railway Analytics & Demand Forecasting - Main Application Controller
 */

const App = {
    state: {
        activePage: 'dashboard',
        routes: [],
        currentForecastRoute: '',
        datasetSummary: null
    },

    init() {
        console.log("Initializing Railway Analytics Platform...");
        this.setupDisplayScale();
        this.setupNavigation();
        this.loadDashboard();
        this.loadFilterMetadata();
        this.setupEventListeners();
    },

    setupDisplayScale() {
        const setScale = (scaleClass) => {
            document.documentElement.classList.remove('scale-normal', 'scale-large', 'scale-presentation');
            document.documentElement.classList.add(scaleClass);
            localStorage.setItem('railway_display_scale_v3', scaleClass);

            document.querySelectorAll('.display-scale-group button').forEach(b => b.classList.remove('active'));
            if (scaleClass === 'scale-normal') document.getElementById('btn-scale-normal')?.classList.add('active');
            if (scaleClass === 'scale-large') document.getElementById('btn-scale-large')?.classList.add('active');
            if (scaleClass === 'scale-presentation') document.getElementById('btn-scale-presentation')?.classList.add('active');
        };

        document.getElementById('btn-scale-normal')?.addEventListener('click', () => setScale('scale-normal'));
        document.getElementById('btn-scale-large')?.addEventListener('click', () => setScale('scale-large'));
        document.getElementById('btn-scale-presentation')?.addEventListener('click', () => setScale('scale-presentation'));

        let savedScale = localStorage.getItem('railway_display_scale_v3');
        if (!savedScale) {
            savedScale = 'scale-presentation'; // Defaults to 130%+ Presentation Scale
            localStorage.setItem('railway_display_scale_v3', 'scale-presentation');
        }
        setScale(savedScale);
    },

    setupNavigation() {
        const navLinks = document.querySelectorAll('.sidebar-nav .nav-link');
        navLinks.forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const targetPage = link.getAttribute('data-page');
                if (targetPage) {
                    this.navigateTo(targetPage);
                }
            });
        });
    },

    navigateTo(pageId) {
        this.state.activePage = pageId;

        // Update Nav UI
        document.querySelectorAll('.sidebar-nav .nav-link').forEach(link => {
            if (link.getAttribute('data-page') === pageId) {
                link.classList.add('active');
            } else {
                link.classList.remove('active');
            }
        });

        // Update Pages
        document.querySelectorAll('.page-view').forEach(view => {
            view.classList.remove('active');
        });
        const targetView = document.getElementById(`page-${pageId}`);
        if (targetView) {
            targetView.classList.add('active');
        }

        // Update Title
        const pageTitles = {
            'dashboard': 'Executive Overview & KPIs',
            'passenger-analytics': 'Passenger Demand Analysis',
            'route-performance': 'Route Performance & Capacity Ranking',
            'demand-forecasting': 'Spark MLlib Demand Forecasting',
            'delay-analysis': 'Train Delay & Punctuality Analysis',
            'coach-occupancy': 'Coach & Train Occupancy Intelligence',
            'smart-recommendations': 'Operational Decision Support Engine',
            'capacity-simulator': 'What-If Demand Surge & Coach Capacity Simulator',
            'spark-profiler': 'Apache Spark 3.5.1 Cluster Engine & Execution Profiler',
            'dataset-management': 'Dataset Ingestion & Spark Processing',
            'reports': 'Intelligence Reports & Exports'
        };
        const titleEl = document.getElementById('topbar-title');
        if (titleEl) {
            titleEl.textContent = pageTitles[pageId] || 'Railway Intelligence Platform';
        }

        // Trigger page data loads
        switch (pageId) {
            case 'dashboard':
                this.loadDashboard();
                break;
            case 'passenger-analytics':
                this.loadPassengerAnalytics();
                break;
            case 'route-performance':
                this.loadRoutePerformance();
                break;
            case 'demand-forecasting':
                this.loadDemandForecasting();
                break;
            case 'delay-analysis':
                this.loadDelayAnalysis();
                break;
            case 'coach-occupancy':
                this.loadCoachOccupancy();
                break;
            case 'smart-recommendations':
                this.loadRecommendations();
                break;
            case 'capacity-simulator':
                this.loadCapacitySimulator();
                break;
            case 'spark-profiler':
                this.loadSparkProfiler();
                break;
            case 'dataset-management':
                this.loadDatasetManagement();
                break;
            case 'reports':
                this.loadReports();
                break;
        }
    },

    // ---------------- MODULE 1: DASHBOARD ----------------
    async loadDashboard() {
        try {
            const summaryRes = await fetch('/api/dashboard/summary');
            const summary = await summaryRes.json();
            this.state.dashboardSummary = summary;

            // Update KPI elements
            document.getElementById('kpi-total-records').textContent = Number(summary.totalRecords || 0).toLocaleString();
            document.getElementById('kpi-total-trains').textContent = Number(summary.totalTrains || 0).toLocaleString();
            document.getElementById('kpi-total-routes').textContent = Number(summary.totalRoutes || 0).toLocaleString();
            document.getElementById('kpi-total-passengers').textContent = Number(summary.totalPassengers || 0).toLocaleString();
            document.getElementById('kpi-avg-occupancy').textContent = (summary.avgOccupancy || 0).toFixed(1) + '%';
            document.getElementById('kpi-avg-delay').textContent = (summary.avgDelay || 0).toFixed(1) + ' min';
            document.getElementById('kpi-highest-route').textContent = summary.highestDemandRoute || 'N/A';
            document.getElementById('kpi-lowest-route').textContent = summary.lowestDemandRoute || 'N/A';

            // Topbar dataset pill
            const statusPill = document.getElementById('topbar-dataset-status');
            if (statusPill) {
                statusPill.textContent = `${Number(summary.totalRecords || 0).toLocaleString()} Records Active`;
            }

            // Load traffic trend
            const trendRes = await fetch('/api/dashboard/traffic-trend');
            const trendData = await trendRes.json();
            Charts.renderTrafficTrend('chart-traffic-trend', trendData);

            // Load route comparison
            const routesRes = await fetch('/api/analytics/top-routes?limit=7');
            const topRoutes = await routesRes.json();
            this.state.routes = topRoutes;
            Charts.renderRouteComparison('chart-route-comparison', topRoutes);

            // Render Occupancy Donut
            if (summary.occupancyDistribution) {
                Charts.renderOccupancyDonut('chart-occupancy-donut', summary.occupancyDistribution);
            }

            // Render Monthly Bar
            if (summary.monthlyPassengerDistribution) {
                Charts.renderMonthlyDistribution('chart-monthly-dist', summary.monthlyPassengerDistribution);
            }

        } catch (error) {
            console.error('Failed to load dashboard:', error);
        }
    },

    // ---------------- MODULE 2: PASSENGER DEMAND ANALYSIS ----------------
    async loadPassengerAnalytics() {
        try {
            // Load Charts
            const monthlyRes = await fetch('/api/analytics/monthly');
            const monthlyData = await monthlyRes.json();
            Charts.renderMonthlyDistribution('chart-pa-monthly', monthlyData);

            const dowRes = await fetch('/api/analytics/weekday-weekend');
            const dowData = await dowRes.json();
            Charts.renderWeekdayVsWeekend('chart-pa-dow', dowData);

            // Trigger initial filter query
            this.filterPassengerRecords();
        } catch (error) {
            console.error('Failed to load passenger analytics:', error);
        }
    },

    async loadFilterMetadata() {
        try {
            const res = await fetch('/api/analytics/metadata');
            const meta = await res.json();

            const populateSelect = (id, items, allLabel) => {
                const el = document.getElementById(id);
                if (!el) return;
                el.innerHTML = `<option value="ALL">${allLabel}</option>`;
                items.forEach(it => {
                    el.innerHTML += `<option value="${it}">${it}</option>`;
                });
            };

            populateSelect('filter-source', meta.sources || [], 'All Sources');
            populateSelect('filter-destination', meta.destinations || [], 'All Destinations');
            populateSelect('filter-train', meta.trains || [], 'All Trains');
            populateSelect('filter-month', meta.months || [], 'All Months');

        } catch (error) {
            console.error('Failed to load filter metadata:', error);
        }
    },

    async filterPassengerRecords() {
        const source = document.getElementById('filter-source')?.value || 'ALL';
        const destination = document.getElementById('filter-destination')?.value || 'ALL';
        const trainId = document.getElementById('filter-train')?.value || 'ALL';
        const month = document.getElementById('filter-month')?.value || 'ALL';
        const startDate = document.getElementById('filter-start-date')?.value || '';
        const endDate = document.getElementById('filter-end-date')?.value || '';

        const tbody = document.getElementById('table-passenger-records-body');
        if (tbody) {
            tbody.innerHTML = '<tr><td colspan="9" class="text-center py-4 text-muted"><div class="spinner-border spinner-border-sm text-primary me-2"></div>Filtering records via Spring Boot & Spark...</td></tr>';
        }

        try {
            const query = new URLSearchParams({
                source, destination, trainId, month, startDate, endDate
            });
            const res = await fetch(`/api/analytics/filter?${query.toString()}`);
            const records = await res.json();

            const countEl = document.getElementById('filtered-records-count');
            if (countEl) countEl.textContent = `${records.length} records matching criteria`;

            if (!tbody) return;
            if (records.length === 0) {
                tbody.innerHTML = '<tr><td colspan="9" class="text-center py-4 text-muted">No records match the selected filter criteria.</td></tr>';
                return;
            }

            // Display top 100 to ensure high client performance
            const displayRecords = records.slice(0, 100);
            tbody.innerHTML = displayRecords.map(r => `
                <tr>
                    <td><strong>${r.recordId}</strong></td>
                    <td>${r.trainId} <br><small class="text-muted">${r.trainName}</small></td>
                    <td>${r.sourceStation} &rarr; ${r.destinationStation}</td>
                    <td>${r.travelDate}</td>
                    <td><strong>${Number(r.passengerCount).toLocaleString()}</strong></td>
                    <td>${Number(r.trainCapacity).toLocaleString()}</td>
                    <td>${r.delayMinutes} min</td>
                    <td>
                        <span class="badge-occupancy ${r.occupancyStatus ? r.occupancyStatus.toLowerCase() : 'low'}">
                            ${r.occupancyPercentage}% (${r.occupancyStatus})
                        </span>
                    </td>
                    <td><span class="badge bg-light text-dark border">${r.trainCategory}</span></td>
                </tr>
            `).join('');

        } catch (error) {
            console.error('Filter failed:', error);
            if (tbody) tbody.innerHTML = '<tr><td colspan="9" class="text-center text-danger py-4">Error loading records.</td></tr>';
        }
    },

    // ---------------- MODULE 3: ROUTE PERFORMANCE ----------------
    async loadRoutePerformance() {
        const sortBy = document.getElementById('route-sort-by')?.value || 'rank';
        const order = document.getElementById('route-sort-order')?.value || 'asc';
        const tbody = document.getElementById('table-route-performance-body');

        if (tbody) {
            tbody.innerHTML = '<tr><td colspan="10" class="text-center py-4 text-muted"><div class="spinner-border spinner-border-sm text-primary me-2"></div>Computing route performance rankings...</td></tr>';
        }

        try {
            const res = await fetch(`/api/routes/performance?sortBy=${sortBy}&order=${order}`);
            const routes = await res.json();

            if (!tbody) return;
            tbody.innerHTML = routes.map(r => `
                <tr>
                    <td><span class="badge ${r.rank <= 3 ? 'bg-primary' : 'bg-light text-dark border'}">#${r.rank}</span></td>
                    <td><strong>${r.route}</strong></td>
                    <td>${Number(r.totalPassengers).toLocaleString()}</td>
                    <td>${Number(r.tripCount).toLocaleString()}</td>
                    <td>${Math.round(r.avgPassengers).toLocaleString()}</td>
                    <td>
                        <div class="d-flex align-items-center gap-2">
                            <span>${r.avgOccupancy}%</span>
                            <div class="progress flex-grow-1" style="height: 6px; width: 60px;">
                                <div class="progress-bar ${r.avgOccupancy > 85 ? 'bg-danger' : (r.avgOccupancy > 70 ? 'bg-warning' : 'bg-success')}" 
                                     style="width: ${Math.min(100, r.avgOccupancy)}%"></div>
                            </div>
                        </div>
                    </td>
                    <td>${r.avgDelay} min</td>
                    <td>${r.distanceKm} km</td>
                    <td>
                        <span class="badge-trend ${r.trend ? r.trend.toLowerCase() : 'stable'}">
                            ${r.trend}
                        </span>
                    </td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary py-0 px-2" onclick="App.forecastSpecificRoute('${r.route}')">
                            <i class="bi bi-graph-up me-1"></i>Forecast
                        </button>
                    </td>
                </tr>
            `).join('');

        } catch (error) {
            console.error('Route performance load failed:', error);
        }
    },

    // ---------------- MODULE 4: DEMAND FORECASTING (SPARK MLLIB) ----------------
    async loadDemandForecasting() {
        try {
            const routesRes = await fetch('/api/forecast/available-routes');
            const routes = await routesRes.json();
            const selectEl = document.getElementById('forecast-route-select');
            if (selectEl) {
                selectEl.innerHTML = routes.map(r => `<option value="${r}">${r}</option>`).join('');
                if (this.state.currentForecastRoute) {
                    selectEl.value = this.state.currentForecastRoute;
                }
            }

            const targetRoute = selectEl?.value || routes[0];
            if (targetRoute) {
                this.runForecast(targetRoute);
            }
        } catch (error) {
            console.error('Failed to init forecasting view:', error);
        }
    },

    forecastSpecificRoute(route) {
        this.state.currentForecastRoute = route;
        this.navigateTo('demand-forecasting');
    },

    async runForecast(route) {
        const spinner = document.getElementById('forecast-loading-indicator');
        const content = document.getElementById('forecast-content-area');
        if (spinner) spinner.classList.remove('d-none');

        try {
            const res = await fetch(`/api/forecast?route=${encodeURIComponent(route)}`);
            const forecast = await res.json();

            if (spinner) spinner.classList.add('d-none');

            if (forecast.status === 'ERROR') {
                alert('Forecasting Error: ' + forecast.warningMessage);
                return;
            }

            // Warning note if sparse
            const warningEl = document.getElementById('forecast-warning-alert');
            if (warningEl) {
                if (forecast.status === 'WARNING') {
                    warningEl.classList.remove('d-none');
                    warningEl.textContent = forecast.warningMessage;
                } else {
                    warningEl.classList.add('d-none');
                }
            }

            // Populate Metrics
            document.getElementById('forecast-selected-route').textContent = forecast.route;
            document.getElementById('forecast-metric-mae').textContent = forecast.mae;
            document.getElementById('forecast-metric-rmse').textContent = forecast.rmse;
            document.getElementById('forecast-metric-r2').textContent = forecast.r2;
            document.getElementById('forecast-metric-train-samples').textContent = Number(forecast.trainingSamples || 0).toLocaleString();
            document.getElementById('forecast-metric-test-samples').textContent = Number(forecast.testingSamples || 0).toLocaleString();
            document.getElementById('forecast-metric-algo').textContent = forecast.algorithm || 'Spark MLlib RandomForestRegressor';

            // Render Chart
            Charts.renderForecastChart('chart-forecast-main', forecast);

            // Populate Next 7 Days Table
            const tbody = document.getElementById('table-forecast-7days-body');
            if (tbody && forecast.next7DaysForecast) {
                tbody.innerHTML = forecast.next7DaysForecast.map(f => {
                    const dt = new Date(f.date);
                    const dayName = dt.toLocaleDateString('en-US', { weekday: 'long' });
                    const passengers = Math.round(f.predictedValue);
                    const coachesNeeded = Math.ceil(passengers / 72); // standard 72-berth coach estimate
                    return `
                        <tr>
                            <td><strong>${f.date}</strong></td>
                            <td>${dayName}</td>
                            <td><strong class="text-primary">${passengers.toLocaleString()}</strong> passengers</td>
                            <td><span class="badge bg-light text-dark border">~${coachesNeeded} Coaches (Est.)</span></td>
                            <td>
                                <span class="badge ${passengers > 1050 ? 'bg-danger' : 'bg-success'}">
                                    ${passengers > 1050 ? 'High Demand Expected' : 'Normal Operations'}
                                </span>
                            </td>
                        </tr>
                    `;
                }).join('');
            }

        } catch (error) {
            console.error('Forecast request failed:', error);
            if (spinner) spinner.classList.add('d-none');
        }
    },

    // ---------------- MODULE 5: TRAIN DELAY ANALYSIS ----------------
    async loadDelayAnalysis() {
        try {
            const allRes = await fetch('/api/delays/all');
            const delays = await allRes.json();

            // Summary metrics
            let totalTrips = 0;
            let punctualTrips = 0;
            let maxDelay = 0;
            delays.forEach(d => {
                totalTrips += d.totalTrips;
                punctualTrips += d.punctualTrips;
                if (d.maxDelay > maxDelay) maxDelay = d.maxDelay;
            });
            const pRate = totalTrips > 0 ? ((punctualTrips / totalTrips) * 100).toFixed(1) : 0;

            document.getElementById('delay-metric-punctuality').textContent = pRate + '%';
            document.getElementById('delay-metric-max').textContent = maxDelay + ' min';

            // Most Delayed table
            const delayedTbody = document.getElementById('table-most-delayed-body');
            if (delayedTbody) {
                const mostDelayed = [...delays].sort((a,b) => b.avgDelay - a.avgDelay).slice(0, 8);
                delayedTbody.innerHTML = mostDelayed.map((d, idx) => `
                    <tr>
                        <td>#${idx + 1}</td>
                        <td><strong>${d.trainName}</strong> (${d.trainId})</td>
                        <td>${d.route}</td>
                        <td><strong class="text-danger">${d.avgDelay} min</strong></td>
                        <td>${d.maxDelay} min</td>
                        <td>${d.punctualityRate}%</td>
                    </tr>
                `).join('');
            }

            // Most Punctual table
            const punctualTbody = document.getElementById('table-most-punctual-body');
            if (punctualTbody) {
                const mostPunctual = [...delays].sort((a,b) => b.punctualityRate - a.punctualityRate).slice(0, 8);
                punctualTbody.innerHTML = mostPunctual.map((d, idx) => `
                    <tr>
                        <td>#${idx + 1}</td>
                        <td><strong>${d.trainName}</strong> (${d.trainId})</td>
                        <td>${d.route}</td>
                        <td><strong class="text-success">${d.avgDelay} min</strong></td>
                        <td>${d.punctualityRate}%</td>
                        <td><span class="badge bg-success">Exemplary</span></td>
                    </tr>
                `).join('');
            }

            // Delay Distribution chart
            const distRes = await fetch('/api/delays/distribution');
            const distMap = await distRes.json();
            Charts.renderDelayDistribution('chart-delay-dist', distMap);

        } catch (error) {
            console.error('Failed to load delay analysis:', error);
        }
    },

    // ---------------- MODULE 6: COACH OCCUPANCY ANALYSIS ----------------
    async loadCoachOccupancy() {
        try {
            const occRes = await fetch('/api/occupancy/all');
            const occs = await occRes.json();

            this.state.allOccupancies = occs;
            this.renderOccupancyTable(occs);

            // High occupancy cards
            const highOccEl = document.getElementById('occupancy-high-cards');
            if (highOccEl) {
                const highTrains = occs.filter(o => o.avgOccupancy >= 85).slice(0, 4);
                highOccEl.innerHTML = highTrains.map(t => `
                    <div class="col-md-6 mb-3">
                        <div class="p-3 border rounded bg-white shadow-sm">
                            <div class="d-flex justify-content-between align-items-center mb-1">
                                <h6 class="mb-0 fw-bold">${t.trainName} (${t.trainId})</h6>
                                <span class="badge-occupancy critical">${t.avgOccupancy}%</span>
                            </div>
                            <small class="text-muted d-block mb-2">${t.route} &bull; ${t.category}</small>
                            <div class="d-flex justify-content-between text-muted small">
                                <span>Avg Passengers: <strong>${t.avgPassengers}</strong></span>
                                <span>Capacity: <strong>${t.avgCapacity}</strong></span>
                            </div>
                        </div>
                    </div>
                `).join('');
            }

            // Underutilised cards
            const underOccEl = document.getElementById('occupancy-low-cards');
            if (underOccEl) {
                const lowTrains = occs.filter(o => o.avgOccupancy < 60).slice(0, 4);
                underOccEl.innerHTML = lowTrains.map(t => `
                    <div class="col-md-6 mb-3">
                        <div class="p-3 border rounded bg-white shadow-sm">
                            <div class="d-flex justify-content-between align-items-center mb-1">
                                <h6 class="mb-0 fw-bold">${t.trainName} (${t.trainId})</h6>
                                <span class="badge-occupancy low">${t.avgOccupancy}%</span>
                            </div>
                            <small class="text-muted d-block mb-2">${t.route} &bull; ${t.category}</small>
                            <div class="d-flex justify-content-between text-muted small">
                                <span>Avg Passengers: <strong>${t.avgPassengers}</strong></span>
                                <span>Capacity: <strong>${t.avgCapacity}</strong></span>
                            </div>
                        </div>
                    </div>
                `).join('');
            }

        } catch (error) {
            console.error('Failed to load occupancy analysis:', error);
        }
    },

    renderOccupancyTable(items) {
        const tbody = document.getElementById('table-occupancy-body');
        if (!tbody) return;

        tbody.innerHTML = items.map(o => `
            <tr>
                <td><strong>${o.trainId}</strong></td>
                <td>${o.trainName}</td>
                <td>${o.route}</td>
                <td><span class="badge bg-light text-dark border">${o.category}</span></td>
                <td>${o.avgCapacity}</td>
                <td>${o.avgPassengers}</td>
                <td><strong>${o.avgOccupancy}%</strong></td>
                <td>
                    <span class="badge-occupancy ${o.occupancyClass.toLowerCase()}">
                        ${o.occupancyClass}
                    </span>
                </td>
            </tr>
        `).join('');
    },

    filterOccupancyTable() {
        const term = document.getElementById('occupancy-search-input')?.value.toLowerCase() || '';
        const cls = document.getElementById('occupancy-class-filter')?.value || 'ALL';

        let filtered = this.state.allOccupancies || [];
        if (cls !== 'ALL') {
            filtered = filtered.filter(o => o.occupancyClass === cls);
        }
        if (term) {
            filtered = filtered.filter(o =>
                o.trainName.toLowerCase().includes(term) ||
                o.trainId.toLowerCase().includes(term) ||
                o.route.toLowerCase().includes(term)
            );
        }
        this.renderOccupancyTable(filtered);
    },

    // ---------------- MODULE 7: SMART RECOMMENDATIONS ----------------
    async loadRecommendations() {
        const container = document.getElementById('recommendations-container');
        if (!container) return;

        try {
            const res = await fetch('/api/recommendations');
            const recs = await res.json();
            this.state.allRecommendations = recs;
            this.renderRecommendationsList(recs);
        } catch (error) {
            console.error('Failed to load recommendations:', error);
        }
    },

    renderRecommendationsList(items) {
        const container = document.getElementById('recommendations-container');
        if (!container) return;

        if (items.length === 0) {
            container.innerHTML = '<div class="alert alert-info">No active recommendations generated for current dataset.</div>';
            return;
        }

        container.innerHTML = items.map(r => `
            <div class="rec-card ${r.priority ? r.priority.toLowerCase() + '-priority' : ''}">
                <div class="rec-header">
                    <span class="badge-priority ${r.priority ? r.priority.toLowerCase() : 'medium'}">${r.priority} Priority</span>
                    <span class="badge bg-light text-dark border">${r.category}</span>
                </div>
                <h5 class="rec-title">${r.title}</h5>
                <div class="rec-subject"><i class="bi bi-train-front me-1"></i>${r.trainOrRoute}</div>
                <div class="rec-reason">${r.reason}</div>
                <div class="rec-data"><i class="bi bi-bar-chart-line me-1 text-primary"></i><strong>Supporting Analytics:</strong> ${r.supportingData}</div>
                <div class="rec-action">
                    <i class="bi bi-arrow-right-circle-fill"></i>
                    <span><strong>Recommended Action:</strong> ${r.suggestedAction}</span>
                </div>
            </div>
        `).join('');
    },

    filterRecommendations(category) {
        const recs = this.state.allRecommendations || [];
        if (category === 'ALL') {
            this.renderRecommendationsList(recs);
        } else {
            this.renderRecommendationsList(recs.filter(r => r.category === category));
        }

        document.querySelectorAll('#rec-filter-pills button').forEach(b => {
            if (b.getAttribute('data-cat') === category) {
                b.classList.remove('btn-outline-primary');
                b.classList.add('btn-primary');
            } else {
                b.classList.remove('btn-primary');
                b.classList.add('btn-outline-primary');
            }
        });
    },

    // ---------------- MODULE 8: DATASET MANAGEMENT ----------------
    async loadDatasetManagement() {
        try {
            const res = await fetch('/api/dataset/status');
            const summary = await res.json();
            this.updateDatasetSummaryUI(summary);
        } catch (error) {
            console.error('Failed to fetch dataset status:', error);
        }
    },

    updateDatasetSummaryUI(summary) {
        if (!summary) return;
        document.getElementById('ds-source-name').textContent = summary.sourceDataset || 'railway_passenger_data.csv';
        document.getElementById('ds-records-loaded').textContent = Number(summary.recordsLoaded || 0).toLocaleString();
        document.getElementById('ds-records-processed').textContent = Number(summary.recordsProcessed || 0).toLocaleString();
        document.getElementById('ds-invalid-records').textContent = Number(summary.invalidRecords || 0).toLocaleString();
        document.getElementById('ds-duplicate-records').textContent = Number(summary.duplicateRecords || 0).toLocaleString();
        document.getElementById('ds-routes-analysed').textContent = Number(summary.routesAnalysed || 0).toLocaleString();
        document.getElementById('ds-processing-time').textContent = (summary.processingTimeMs || 0) + ' ms';
        document.getElementById('ds-status-badge').textContent = summary.status || 'READY';
    },

    async handleCsvPreview(file) {
        const previewArea = document.getElementById('csv-preview-area');
        const previewInfo = document.getElementById('csv-preview-info');
        const previewTbody = document.getElementById('table-csv-preview-body');
        const processBtn = document.getElementById('btn-process-spark');

        const formData = new FormData();
        formData.append('file', file);

        try {
            previewInfo.innerHTML = '<div class="spinner-border spinner-border-sm text-primary me-2"></div>Validating CSV schema and records...';
            previewArea.classList.remove('d-none');

            const res = await fetch('/api/dataset/preview', {
                method: 'POST',
                body: formData
            });
            const data = await res.json();

            if (!data.valid) {
                previewInfo.innerHTML = `
                    <div class="alert alert-danger mb-3">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                        <strong>Validation Failed:</strong> ${data.message}
                        ${data.errors ? `<ul class="mb-0 mt-2">${data.errors.map(e => `<li>${e}</li>`).join('')}</ul>` : ''}
                    </div>
                `;
                if (processBtn) processBtn.disabled = true;
                return;
            }

            previewInfo.innerHTML = `
                <div class="alert alert-success mb-3">
                    <i class="bi bi-check-circle-fill me-2"></i>
                    <strong>Validation Successful:</strong> ${Number(data.validRows).toLocaleString()} valid records detected.
                    (${data.missingValueRows} missing rows, ${data.duplicateRows} duplicate IDs, ${data.invalidDataTypeRows} type errors ignored).
                </div>
            `;

            if (previewTbody && data.preview) {
                previewTbody.innerHTML = data.preview.map(r => `
                    <tr>
                        <td>${r.recordId}</td>
                        <td>${r.trainId}</td>
                        <td>${r.trainName}</td>
                        <td>${r.route}</td>
                        <td>${r.travelDate}</td>
                        <td>${r.passengerCount}</td>
                        <td>${r.trainCapacity}</td>
                        <td>${r.delayMinutes} min</td>
                        <td>${r.occupancyPercentage}</td>
                    </tr>
                `).join('');
            }

            if (processBtn) {
                processBtn.disabled = false;
                processBtn.onclick = () => this.handleCsvProcess(file);
            }

        } catch (error) {
            console.error('Preview failed:', error);
            previewInfo.innerHTML = '<div class="alert alert-danger">Error previewing CSV file.</div>';
        }
    },

    async handleCsvProcess(file) {
        const processBtn = document.getElementById('btn-process-spark');
        if (processBtn) {
            processBtn.disabled = true;
            processBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Processing via Apache Spark...';
        }

        const formData = new FormData();
        formData.append('file', file);

        try {
            const res = await fetch('/api/dataset/upload', {
                method: 'POST',
                body: formData
            });
            const summary = await res.json();

            if (processBtn) {
                processBtn.innerHTML = '<i class="bi bi-cpu me-2"></i>Process Dataset with Apache Spark';
            }

            alert('Apache Spark Processing Completed!\n' + summary.message);
            this.updateDatasetSummaryUI(summary);
            this.loadDashboard();

        } catch (error) {
            console.error('Processing failed:', error);
            alert('Dataset processing error.');
            if (processBtn) {
                processBtn.disabled = false;
                processBtn.innerHTML = '<i class="bi bi-cpu me-2"></i>Process Dataset with Apache Spark';
            }
        }
    },

    async resetDefaultDataset() {
        if (!confirm('Reset current dataset to default 7,500 simulated records?')) return;
        try {
            const res = await fetch('/api/dataset/reset', { method: 'POST' });
            const summary = await res.json();
            alert('Reset Completed!\n' + summary.message);
            this.updateDatasetSummaryUI(summary);
            this.loadDashboard();
        } catch (error) {
            console.error('Reset failed:', error);
        }
    },

    // ---------------- MODULE 9: REPORTS ----------------
    async loadReports() {
        try {
            const res = await fetch('/api/reports/comprehensive');
            const data = await res.json();

            document.getElementById('report-generated-at').textContent = data.generatedAt;
            document.getElementById('report-total-passengers').textContent = Number(data.dashboardMetrics?.totalPassengers || 0).toLocaleString();
            document.getElementById('report-total-records').textContent = Number(data.dashboardMetrics?.totalRecords || 0).toLocaleString();
            document.getElementById('report-avg-occupancy').textContent = (data.dashboardMetrics?.avgOccupancy || 0) + '%';
            document.getElementById('report-avg-delay').textContent = (data.dashboardMetrics?.avgDelay || 0) + ' min';

            // Top routes in report
            const rTbody = document.getElementById('report-top-routes-body');
            if (rTbody && data.routeStatistics) {
                rTbody.innerHTML = data.routeStatistics.slice(0, 10).map(r => `
                    <tr>
                        <td>#${r.rank}</td>
                        <td><strong>${r.route}</strong></td>
                        <td>${Number(r.totalPassengers).toLocaleString()}</td>
                        <td>${r.avgOccupancy}%</td>
                        <td>${r.avgDelay} min</td>
                        <td>${r.trend}</td>
                    </tr>
                `).join('');
            }

            // Recommendations in report
            const recTbody = document.getElementById('report-recommendations-body');
            if (recTbody && data.recommendations) {
                recTbody.innerHTML = data.recommendations.map(r => `
                    <tr>
                        <td><span class="badge ${r.priority === 'HIGH' ? 'bg-danger' : (r.priority === 'MEDIUM' ? 'bg-warning text-dark' : 'bg-success')}">${r.priority}</span></td>
                        <td><strong>${r.title}</strong><br><small class="text-muted">${r.trainOrRoute}</small></td>
                        <td>${r.reason}</td>
                        <td>${r.suggestedAction}</td>
                    </tr>
                `).join('');
            }

        } catch (error) {
            console.error('Failed to load comprehensive report:', error);
        }
    },

    // ---------------- MODULE 10: WHAT-IF CAPACITY SIMULATOR ----------------
    async loadCapacitySimulator() {
        try {
            const select = document.getElementById('sim-route-select');
            if (select && select.children.length <= 1) {
                const routesRes = await fetch('/api/forecast/available-routes');
                const routes = await routesRes.json();
                select.innerHTML = routes.map(r => `<option value="${r}">${r}</option>`).join('');
            }

            const slider = document.getElementById('sim-surge-slider');
            const label = document.getElementById('sim-surge-label');
            if (slider && label) {
                slider.oninput = () => {
                    label.textContent = `+${slider.value}%`;
                };
            }

            const simBtn = document.getElementById('btn-run-simulation');
            if (simBtn) {
                simBtn.onclick = () => this.runSimulation();
            }

            // Run initial simulation
            await this.runSimulation();

        } catch (error) {
            console.error('Failed to load capacity simulator:', error);
        }
    },

    async runSimulation() {
        const route = document.getElementById('sim-route-select')?.value || 'Bengaluru -> Kolkata';
        const surge = document.getElementById('sim-surge-slider')?.value || 25;
        const coaches = document.getElementById('sim-coaches-select')?.value || 2;

        try {
            const res = await fetch(`/api/spark/simulate?route=${encodeURIComponent(route)}&surgePercent=${surge}&extraCoaches=${coaches}`);
            const data = await res.json();

            const routeEl = document.getElementById('sim-res-route');
            if (routeEl) routeEl.textContent = data.route;

            const badge = document.getElementById('sim-res-badge');
            if (badge) {
                badge.textContent = data.status;
                badge.className = `badge bg-${data.badgeClass || 'success'} fs-6 py-2 px-3`;
            }

            const baseDemandEl = document.getElementById('sim-res-baseline-demand');
            if (baseDemandEl) baseDemandEl.textContent = Number(data.baselineDemand).toLocaleString();

            const simDemandEl = document.getElementById('sim-res-simulated-demand');
            if (simDemandEl) simDemandEl.textContent = Number(data.simulatedDemand).toLocaleString();

            const surgeSubEl = document.getElementById('sim-res-surge-subtext');
            if (surgeSubEl) surgeSubEl.textContent = `With +${data.surgePercent}% passenger surge`;

            const simCapEl = document.getElementById('sim-res-simulated-capacity');
            if (simCapEl) simCapEl.textContent = Number(data.simulatedCapacity).toLocaleString();

            const addedSeatsEl = document.getElementById('sim-res-added-seats');
            if (addedSeatsEl) addedSeatsEl.textContent = `Baseline + ${data.addedSeats} extra berths`;

            const occEl = document.getElementById('sim-res-occupancy');
            if (occEl) {
                occEl.textContent = `${data.simulatedOccupancy}%`;
                occEl.className = data.simulatedOccupancy > 100 ? 'stat-value text-danger' : (data.simulatedOccupancy >= 80 ? 'stat-value text-success' : 'stat-value text-warning');
            }

            const recEl = document.getElementById('sim-res-recommendation');
            if (recEl) recEl.textContent = data.recommendation;

            const revEl = document.getElementById('sim-res-revenue');
            if (revEl) revEl.textContent = `₹${Number(data.estimatedExtraRevenue).toLocaleString()}`;

            const unmetEl = document.getElementById('sim-res-unmet');
            if (unmetEl) unmetEl.textContent = `${Number(data.unmetDemand).toLocaleString()} passengers`;

        } catch (error) {
            console.error('Simulation error:', error);
        }
    },

    // ---------------- MODULE 11: SPARK ENGINE PROFILER ----------------
    async loadSparkProfiler() {
        try {
            const res = await fetch('/api/spark/profiler');
            const data = await res.json();

            const vEl = document.getElementById('spark-prof-version');
            if (vEl) vEl.textContent = `${data.appName} (${data.sparkVersion})`;

            const mEl = document.getElementById('spark-prof-master');
            if (mEl) mEl.textContent = `Master: ${data.master} | Java 21 LTS`;

            const cEl = document.getElementById('spark-prof-cores');
            if (cEl) cEl.textContent = `${data.availableCores} Cores`;

            const pEl = document.getElementById('spark-prof-parallelism');
            if (pEl) pEl.textContent = `Default Parallelism: ${data.defaultParallelism} Threads`;

            const partEl = document.getElementById('spark-prof-partitions');
            if (partEl) partEl.textContent = `${data.activePartitions} Partitions`;

            const recEl = document.getElementById('spark-prof-records');
            if (recEl) recEl.textContent = `${Number(data.totalRecords).toLocaleString()} records processed`;

            if (data.memory) {
                const memEl = document.getElementById('spark-prof-memory');
                if (memEl) memEl.textContent = `${data.memory.usedMemoryMb} MB / ${data.memory.maxMemoryMb} MB`;

                const memSubEl = document.getElementById('spark-prof-memory-sub');
                if (memSubEl) memSubEl.textContent = `JVM Heap: ${data.memory.freeMemoryMb} MB Free`;
            }

            // Catalyst Plan
            const planEl = document.getElementById('spark-catalyst-plan');
            if (planEl) {
                planEl.textContent = data.catalystPlan || 'Catalyst execution plan active.';
            }

            // Partitions table
            const tbody = document.getElementById('table-spark-partitions-body');
            if (tbody && data.partitions) {
                tbody.innerHTML = data.partitions.map(p => `
                    <tr>
                        <td><strong>Partition #${p.partitionId}</strong></td>
                        <td><strong>${Number(p.recordCount).toLocaleString()}</strong> records</td>
                        <td><span class="badge bg-light text-dark border">${p.storageStatus}</span></td>
                        <td><span class="badge bg-success"><i class="bi bi-check-circle me-1"></i>${p.status}</span></td>
                    </tr>
                `).join('');
            }

            const refBtn = document.getElementById('btn-refresh-profiler');
            if (refBtn) {
                refBtn.onclick = () => this.loadSparkProfiler();
            }

        } catch (error) {
            console.error('Failed to load Spark profiler:', error);
        }
    },

    setupEventListeners() {
        // Passenger analytics filter buttons
        document.getElementById('btn-apply-filter')?.addEventListener('click', () => {
            this.filterPassengerRecords();
        });
        document.getElementById('btn-reset-filter')?.addEventListener('click', () => {
            document.getElementById('filter-source').value = 'ALL';
            document.getElementById('filter-destination').value = 'ALL';
            document.getElementById('filter-train').value = 'ALL';
            document.getElementById('filter-month').value = 'ALL';
            document.getElementById('filter-start-date').value = '';
            document.getElementById('filter-end-date').value = '';
            this.filterPassengerRecords();
        });

        // Route sorting
        document.getElementById('route-sort-by')?.addEventListener('change', () => this.loadRoutePerformance());
        document.getElementById('route-sort-order')?.addEventListener('change', () => this.loadRoutePerformance());

        // Forecast route change
        document.getElementById('forecast-route-select')?.addEventListener('change', (e) => {
            this.runForecast(e.target.value);
        });

        // Occupancy table search & filter
        document.getElementById('occupancy-search-input')?.addEventListener('input', () => this.filterOccupancyTable());
        document.getElementById('occupancy-class-filter')?.addEventListener('change', () => this.filterOccupancyTable());

        // Recommendation category filters
        document.querySelectorAll('#rec-filter-pills button').forEach(btn => {
            btn.addEventListener('click', () => {
                const cat = btn.getAttribute('data-cat');
                this.filterRecommendations(cat);
            });
        });

        // File upload dropzone
        const dropzone = document.getElementById('upload-dropzone');
        const fileInput = document.getElementById('file-upload-input');

        if (dropzone && fileInput) {
            dropzone.addEventListener('click', () => fileInput.click());
            dropzone.addEventListener('dragover', (e) => {
                e.preventDefault();
                dropzone.classList.add('dragover');
            });
            dropzone.addEventListener('dragleave', () => dropzone.classList.remove('dragover'));
            dropzone.addEventListener('drop', (e) => {
                e.preventDefault();
                dropzone.classList.remove('dragover');
                if (e.dataTransfer.files.length > 0) {
                    this.handleCsvPreview(e.dataTransfer.files[0]);
                }
            });
            fileInput.addEventListener('change', (e) => {
                if (e.target.files.length > 0) {
                    this.handleCsvPreview(e.target.files[0]);
                }
            });
        }

        // Reset default dataset button
        document.getElementById('btn-reset-default-ds')?.addEventListener('click', () => this.resetDefaultDataset());
    }
};

document.addEventListener('DOMContentLoaded', () => {
    App.init();
});

