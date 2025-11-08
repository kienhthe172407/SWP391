# Request Management Use Cases Documentation

## 2.10.1 Submit Request

**Primary Actors**
Employee

**Secondary Actors**
None

**Description**
As an employee, I want to submit leave/remote work requests so that I can request time off or work arrangements that require manager approval.

**Preconditions**
- User authentication: User is logged in as Employee.
- Employee record exists in system.
- Request types are configured (Annual Leave, Sick Leave, Remote Work, etc.).

**Postconditions**

Success:
  - Request created with status "Pending".
  - Employee can view request in their request list.
  - Manager can see request for approval.

Failure:
  - Request not created.
  - Error message displayed.
  - User remains on submit form.

**Normal Sequence/Flow**

1. Employee navigates to Submit Request page.

2. System displays form:
   - Request Type dropdown (Annual Leave, Sick Leave, Remote Work, Business Trip)
   - Start Date picker
   - End Date picker
   - Number of Days input (auto-calculated or manual)
   - Reason textarea (required, min 10 characters)

3. Employee selects request type and dates.

4. Employee enters number of days and reason.

5. Employee clicks Submit.

6. System validates:
   - All required fields filled
   - End date not before start date
   - Number of days > 0
   - Reason length >= 10 characters

7. System checks annual quota (if applicable):
   - Calculates approved + pending days for request type in current year
   - Verifies new request doesn't exceed max days per year
   - Shows remaining quota if exceeded

8. System creates Request record:
   - Sets status to "Pending"
   - Records employee_id, dates, reason
   - Sets created_at timestamp

9. System displays success message and redirects to Request List.

**Alternative Sequences/Flows**

A1: Missing Required Fields
  - At step 6, validation fails
  - Error message displayed
  - Form data preserved
  - User corrects and resubmits

A2: End Date Before Start Date
  - At step 6, end date < start date
  - Error: "End date cannot be before start date"
  - User corrects dates

A3: Exceeds Annual Quota
  - At step 7, total days exceed limit
  - Error shows: max allowed, already used, remaining, requested
  - Request not created
  - User adjusts number of days

A4: Invalid Request Type
  - At step 6, request type not found
  - Error: "Invalid request type selected"
  - User selects valid type

A5: Number of Days Zero or Negative
  - At step 6, numberOfDays <= 0
  - Error: "Number of days must be greater than 0"
  - User enters valid value

---

## 2.10.2 View Request List

**Primary Actors**
Employee

**Secondary Actors**
None

**Description**
As an employee, I want to view all my submitted requests with their statuses so that I can track my leave/work requests.

**Preconditions**
- User authentication: User is logged in as Employee.
- Employee record exists in system.

**Postconditions**
- Request list displayed with current statuses.
- Statistics shown (total, pending, approved, rejected).
- User can filter and search requests.

**Normal Sequence/Flow**

1. Employee navigates to My Requests page.

2. System retrieves all requests for employee:
   - Queries requests table by employee_id
   - Joins with request_types for type names
   - Orders by created_at DESC (newest first)

3. System calculates statistics:
   - Total requests count
   - Pending count
   - Approved count
   - Rejected count

4. System displays request list with columns:
   - Request ID
   - Request Type (Annual Leave, Sick Leave, etc.)
   - Start Date - End Date
   - Number of Days
   - Status (badge: Pending=yellow, Approved=green, Rejected=red)
   - Submitted Date
   - Actions (View, Edit, Cancel)

5. System shows statistics cards at top.

6. Employee can apply filters:
   - Status dropdown (All, Pending, Approved, Rejected)
   - Request Type dropdown
   - Date range (Start Date, End Date)

7. Employee can click actions:
   - View: See request details
   - Edit: Modify pending request (Use Case 2.10.4)
   - Cancel: Cancel pending request (Use Case 2.10.5)

**Alternative Sequences/Flows**

A1: No Requests Found
  - At step 2, no requests exist
  - Message: "You haven't submitted any requests yet"
  - Shows "Submit New Request" button

A2: Filter Results Empty
  - At step 6, filters match no requests
  - Message: "No requests found matching filters"
  - User can clear filters

A3: Only Approved/Rejected Requests
  - Edit and Cancel buttons disabled
  - Only View button available

---

## 2.10.3 Approve/Reject Request

**Primary Actors**
Department Manager, HR Manager

**Secondary Actors**
None

**Description**
As a Department Manager/HR Manager, I want to approve or reject employee requests so that I can manage team absences and work arrangements.

**Preconditions**
- User authentication: User is logged in as Dept Manager or HR Manager.
- Pending requests exist.
- Manager has authority over employee's department (for Dept Manager).

**Postconditions**

Success (Approve):
  - Request status changed to "Approved".
  - Reviewed_by, review_comment, reviewed_at recorded.
  - Employee notified.

Success (Reject):
  - Request status changed to "Rejected".
  - Review comment required.
  - Employee can see rejection reason.

**Normal Sequence/Flow**

1. Manager navigates to Pending Requests page.

2. System displays pending requests:
   - For Dept Manager: Only requests from their department
   - For HR Manager: All pending requests
   - Shows: Employee, Type, Dates, Days, Reason

3. Manager reviews request details.

4. Manager clicks Approve or Reject button.

5. System displays confirmation modal:
   - Request summary
   - Review Comment textarea (optional for approve, required for reject)
   - Confirm button

6. Manager enters comment (if any) and confirms.

7. System validates:
   - User has appropriate role
   - Request still pending
   - Dept Manager: Employee in same department
   - Reject: Comment not empty

8. System updates request:
   - Sets status to "Approved" or "Rejected"
   - Records reviewed_by (user_id)
   - Sets reviewed_at timestamp
   - Saves review_comment

9. System displays success message and refreshes list.

**Alternative Sequences/Flows**

A1: Request Already Processed
  - At step 7, request no longer pending
  - Error: "This request has already been [status]"
  - Manager redirected to list

A2: Unauthorized Department
  - At step 7, Dept Manager tries to approve request from different department
  - Error: "You can only approve/reject requests from your department"
  - Action blocked

A3: Missing Rejection Comment
  - At step 7, reject without comment
  - Error: "Rejection reason is required"
  - Manager must enter comment

A4: Concurrent Processing
  - Two managers try to process same request
  - First transaction succeeds
  - Second gets error: "Request already processed"

---

## 2.10.4 Edit Request (Before Approval)

**Primary Actors**
Employee

**Secondary Actors**
None

**Description**
As an employee, I want to edit my pending requests so that I can correct mistakes or update dates before manager approval.

**Preconditions**
- User authentication: User is logged in as Employee.
- Request exists and belongs to user.
- Request status is "Pending".

**Postconditions**

Success:
  - Request updated with new information.
  - Status remains "Pending".
  - Updated_at timestamp refreshed.

Failure:
  - Request not modified.
  - Error message shown.

**Normal Sequence/Flow**

1. Employee views request list (Use Case 2.10.2).

2. Employee clicks Edit button on pending request.

3. System validates:
   - Request exists
   - Request belongs to employee
   - Status is "Pending"

4. System displays edit form pre-filled with:
   - Request Type (selected)
   - Start Date
   - End Date
   - Number of Days
   - Reason

5. Employee modifies fields as needed.

6. Employee clicks Update button.

7. System validates (same as submit):
   - Required fields filled
   - Date range valid
   - Number of days > 0
   - Reason length >= 10
   - Annual quota not exceeded

8. System updates request record:
   - Updates modified fields
   - Sets updated_at timestamp
   - Status remains "Pending"

9. System displays success message and redirects to request list.

**Alternative Sequences/Flows**

A1: Request Not Pending
  - At step 3, status is Approved/Rejected/Cancelled
  - Error: "Only pending requests can be edited"
  - Redirected to request list

A2: Wrong Employee
  - At step 3, request belongs to different employee
  - Error: "You don't have permission to edit this request"
  - Access denied

A3: Validation Fails
  - At step 7, new data invalid
  - Error message shown
  - Form redisplayed with entered data
  - Employee corrects and resubmits

A4: Exceeds Quota After Edit
  - At step 7, modified days exceed annual limit
  - Error shows quota details
  - Employee adjusts days

A5: Request ID Not Found
  - At step 3, invalid request ID
  - Error: "Request not found"
  - Redirected to list

---

## 2.10.5 Cancel Request (Before Approval)

**Primary Actors**
Employee

**Secondary Actors**
None

**Description**
As an employee, I want to cancel my pending requests so that I can withdraw requests I no longer need.

**Preconditions**
- User authentication: User is logged in as Employee.
- Request exists and belongs to user.
- Request status is "Pending".

**Postconditions**

Success:
  - Request status changed to "Cancelled".
  - Cancelled_at timestamp recorded.
  - Request no longer appears in pending list.

Failure:
  - Request not cancelled.
  - Error message shown.

**Normal Sequence/Flow**

1. Employee views request list (Use Case 2.10.2).

2. Employee clicks Cancel button on pending request.

3. System displays confirmation dialog:
   - "Are you sure you want to cancel this request?"
   - Shows request details (Type, Dates, Days)
   - Confirm and Cancel buttons

4. Employee clicks Confirm.

5. System validates:
   - Request exists
   - Request belongs to employee
   - Status is "Pending"

6. System updates request:
   - Sets status to "Cancelled"
   - Records cancelled_at timestamp

7. System displays success message: "Request cancelled successfully!"

8. System redirects to request list.

**Alternative Sequences/Flows**

A1: Request Not Pending
  - At step 5, status is Approved/Rejected/Cancelled
  - Error: "Only pending requests can be cancelled"
  - No changes made

A2: Wrong Employee
  - At step 5, request belongs to different employee
  - Error: "You don't have permission to cancel this request"
  - Access denied

A3: User Cancels Confirmation
  - At step 4, employee clicks Cancel in dialog
  - No action taken
  - Dialog closes

A4: Request Not Found
  - At step 5, invalid request ID
  - Error: "Request not found"
  - Redirected to list

A5: Database Update Fails
  - At step 6, update operation fails
  - Error: "Failed to cancel request. The request may no longer be pending"
  - User can retry

---

## 2.12.1 View Company Information

**Primary Actors**
Guest, Employee, HR Staff, HR Manager, Department Manager

**Secondary Actors**
None

**Description**
As a guest or any user, I want to view company information so that I can learn about the company's profile, mission, vision, and contact details.

**Preconditions**
- No authentication required (accessible to all visitors).
- Active company information exists in database.

**Postconditions**
- Company information page displayed with all details.
- User can view company profile and contact information.

**Normal Sequence/Flow**

1. User navigates to Company Information page (URL: /company-info).

2. System retrieves active company information:
   - Queries company_information table
   - Filters by is_active = TRUE
   - Returns single active record

3. System displays company information:
   - Company Name and Logo
   - About Us section
   - Mission Statement
   - Vision Statement
   - Core Values
   - Contact Information (Address, Phone, Email, Website)
   - Company Details (Founded Year, Number of Employees, Industry)
   - Social Media Links (if available)

4. User views information.

**Alternative Sequences/Flows**

A1: No Active Company Information
  - At step 2, no active record found
  - Page displays without company details
  - Shows generic message or empty state

A2: Database Error
  - At step 2, database query fails
  - Error message: "An error occurred while loading company information"
  - Page still loads but without data
  - Error logged to console

A3: Missing Optional Fields
  - Some fields (founded year, social media) may be null
  - System handles gracefully by not displaying those sections
  - Page renders normally with available information