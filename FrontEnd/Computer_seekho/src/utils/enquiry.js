// The backend's Enquiry entity has no course/program column (see
// api/enquiries.js and the public Enquiry form), so "Program interested
// in" is folded into enquirerQuery as a header line. These helpers read
// that structure back out for display, and derive a Kanban-style status
// from the fields that DO exist (enquiryProcessedFlag, inquiryCounter,
// followupDate, closureReason) since there's no stored status column either.

export function extractProgramInterest(query) {
  if (!query) return '';
  const match = query.match(/^Program interested in:\s*(.+)$/m);
  return match ? match[1].trim() : '';
}

export function extractPreferredTime(query) {
  if (!query) return '';
  const match = query.match(/^Preferred time to call:\s*(.+)$/m);
  return match ? match[1].trim() : '';
}

// Strips the folded-in header lines back out, leaving just the free-text
// remarks a person actually typed.
export function extractFreeText(query) {
  if (!query) return '';
  return query
    .split('\n')
    .filter((line) => !/^Program interested in:/.test(line) && !/^Preferred time to call:/.test(line))
    .join('\n')
    .trim();
}

const todayISO = () => new Date().toISOString().slice(0, 10);

export function deriveEnquiryStatus(e) {
  if (e.enquiryProcessedFlag) {
    return e.closureReason || e.closureReasonText ? 'closed' : 'registered';
  }
  const counter = e.inquiryCounter || 0;
  const isDue = e.followupDate && e.followupDate <= todayISO();
  if (isDue) return counter > 0 ? 'followup-due' : 'new';
  return counter > 0 ? 'contacted' : 'new';
}

export const STATUS_COPY = {
  new: 'New',
  contacted: 'Contacted',
  'followup-due': 'Follow-up due',
  registered: 'Registered',
  closed: 'Closed',
};
