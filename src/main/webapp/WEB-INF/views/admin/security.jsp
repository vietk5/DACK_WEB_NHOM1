<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../layout_admin_header.jspf" %>

<style>
    .stat-card {
        background: rgba(20, 20, 30, 0.8);
        border: 1px solid rgba(255,255,255,0.1);
        border-radius: 12px;
        padding: 1.5rem;
        transition: all 0.3s;
    }
    .stat-card:hover {
        border-color: rgba(0,212,255,0.5);
        transform: translateY(-2px);
    }
    .stat-value {
        font-size: 2.5rem;
        font-weight: 700;
        font-family: 'Orbitron', sans-serif;
    }
    .event-table {
        background: rgba(20, 20, 30, 0.6);
        border-radius: 8px;
        overflow: hidden;
    }
    .badge-level-INFO { background: #17a2b8; }
    .badge-level-WARN { background: #ffc107; color: #000; }
    .badge-level-ERROR { background: #dc3545; }
    .ip-badge {
        background: rgba(0,212,255,0.2);
        color: #00d4ff;
        padding: 0.25rem 0.5rem;
        border-radius: 4px;
        font-family: 'Courier New', monospace;
        font-size: 0.85rem;
    }
    .event-type-badge {
        font-size: 0.75rem;
        padding: 0.35rem 0.6rem;
    }
    .chart-container {
        background: rgba(20, 20, 30, 0.6);
        border-radius: 8px;
        padding: 1.5rem;
        margin-bottom: 1.5rem;
    }
</style>

<div class="container-fluid">
    <!-- Page Header -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="mb-1">
                <i class="bi bi-shield-lock text-primary me-2"></i>
                Security Monitoring
            </h2>
            <p class="text-muted mb-0">SIEM Dashboard - Real-time Security Event Management</p>
        </div>
        <div>
            <button class="btn btn-outline-primary" onclick="refreshData()">
                <i class="bi bi-arrow-clockwise me-1"></i>Refresh
            </button>
            <button class="btn btn-outline-danger" onclick="exportLogs()">
                <i class="bi bi-download me-1"></i>Export
            </button>
        </div>
    </div>

    <!-- Statistics Cards -->
    <div class="row g-3 mb-4">
        <div class="col-md-3">
            <div class="stat-card">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <div class="text-muted small mb-1">Total Events</div>
                        <div class="stat-value text-info">${totalEvents}</div>
                    </div>
                    <i class="bi bi-activity fs-1 text-info opacity-50"></i>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <div class="text-muted small mb-1">Errors</div>
                        <div class="stat-value text-danger">${errorCount}</div>
                    </div>
                    <i class="bi bi-exclamation-triangle fs-1 text-danger opacity-50"></i>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <div class="text-muted small mb-1">Warnings</div>
                        <div class="stat-value text-warning">${warnCount}</div>
                    </div>
                    <i class="bi bi-exclamation-circle fs-1 text-warning opacity-50"></i>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <div class="text-muted small mb-1">Info</div>
                        <div class="stat-value text-success">${infoCount}</div>
                    </div>
                    <i class="bi bi-info-circle fs-1 text-success opacity-50"></i>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <!-- Event Type Distribution -->
        <div class="col-md-6 mb-4">
            <div class="chart-container">
                <h5 class="mb-3">
                    <i class="bi bi-pie-chart me-2"></i>Event Type Distribution
                </h5>
                <div class="table-responsive">
                    <table class="table table-dark table-sm">
                        <thead>
                            <tr>
                                <th>Event Type</th>
                                <th class="text-end">Count</th>
                                <th style="width: 40%">Distribution</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="stat" items="${eventTypeStats}">
                                <tr>
                                    <td><span class="badge event-type-badge bg-secondary">${stat.key}</span></td>
                                    <td class="text-end">${stat.value}</td>
                                    <td>
                                        <div class="progress" style="height: 20px;">
                                            <div class="progress-bar bg-info" 
                                                 style="width: ${(stat.value * 100) / totalEvents}%">
                                                ${String.format("%.1f", (stat.value * 100.0) / totalEvents)}%
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Top Suspicious IPs -->
        <div class="col-md-6 mb-4">
            <div class="chart-container">
                <h5 class="mb-3">
                    <i class="bi bi-geo-alt me-2"></i>Top Suspicious IP Addresses
                </h5>
                <div class="table-responsive">
                    <table class="table table-dark table-sm">
                        <thead>
                            <tr>
                                <th>IP Address</th>
                                <th class="text-end">Failed Attempts</th>
                                <th class="text-center">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="ip" items="${topSuspiciousIPs}">
                                <tr>
                                    <td><span class="ip-badge">${ip.key}</span></td>
                                    <td class="text-end">
                                        <span class="badge bg-danger">${ip.value}</span>
                                    </td>
                                    <td class="text-center">
                                        <button class="btn btn-sm btn-outline-warning" 
                                                onclick="filterByIP('${ip.key}')">
                                            <i class="bi bi-funnel"></i>
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty topSuspiciousIPs}">
                                <tr>
                                    <td colspan="3" class="text-center text-muted">
                                        <i class="bi bi-check-circle me-2"></i>No suspicious activity detected
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Filters -->
    <div class="card bg-dark border-secondary mb-3">
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/admin/security" class="row g-3">
                <div class="col-md-3">
                    <label class="form-label small">Event Type</label>
                    <select name="type" class="form-select form-select-sm">
                        <option value="">All Types</option>
                        <option value="LOGIN_SUCCESS">Login Success</option>
                        <option value="LOGIN_FAILURE">Login Failure</option>
                        <option value="ACCOUNT_LOCKOUT">Account Lockout</option>
                        <option value="REGISTRATION">Registration</option>
                        <option value="RATE_LIMIT_EXCEEDED">Rate Limit</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label small">Level</label>
                    <select name="level" class="form-select form-select-sm">
                        <option value="">All Levels</option>
                        <option value="INFO">Info</option>
                        <option value="WARN">Warning</option>
                        <option value="ERROR">Error</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label small">IP Address</label>
                    <input type="text" name="ip" class="form-control form-control-sm" 
                           placeholder="e.g., 192.168.1.1">
                </div>
                <div class="col-md-3">
                    <label class="form-label small">&nbsp;</label>
                    <div>
                        <button type="submit" class="btn btn-primary btn-sm">
                            <i class="bi bi-funnel me-1"></i>Filter
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/security" 
                           class="btn btn-secondary btn-sm">
                            <i class="bi bi-x-circle me-1"></i>Clear
                        </a>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Events Table -->
    <div class="event-table">
        <div class="table-responsive">
            <table class="table table-dark table-hover mb-0">
                <thead>
                    <tr>
                        <th style="width: 180px">Timestamp</th>
                        <th>Event Type</th>
                        <th>Level</th>
                        <th>Email</th>
                        <th>IP Address</th>
                        <th>Details</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="event" items="${events}">
                        <tr>
                            <td class="small text-muted">
                                <i class="bi bi-clock me-1"></i>
                                ${event.timestamp.toString().replace('T', ' ').substring(0, 19)}
                            </td>
                            <td>
                                <span class="badge event-type-badge 
                                    ${event.eventType == 'LOGIN_SUCCESS' ? 'bg-success' : 
                                      event.eventType == 'LOGIN_FAILURE' ? 'bg-warning' : 
                                      event.eventType == 'ACCOUNT_LOCKOUT' ? 'bg-danger' : 'bg-secondary'}">
                                    ${event.eventType}
                                </span>
                            </td>
                            <td>
                                <span class="badge badge-level-${event.level}">${event.level}</span>
                            </td>
                            <td class="small">${event.email}</td>
                            <td><span class="ip-badge">${event.ipAddress}</span></td>
                            <td class="small text-muted">${event.details}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty events}">
                        <tr>
                            <td colspan="6" class="text-center text-muted py-4">
                                <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                                No security events found
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
function refreshData() {
    location.reload();
}

function filterByIP(ip) {
    window.location.href = '${pageContext.request.contextPath}/admin/security?ip=' + ip;
}

function exportLogs() {
    alert('Export functionality - Coming soon!\nThis will export logs to CSV/JSON format.');
}

// Auto-refresh every 30 seconds
setInterval(function() {
    console.log('Auto-refreshing security data...');
    // You can implement AJAX refresh here
}, 30000);
</script>

<%@ include file="../layout_admin_footer.jspf" %>
