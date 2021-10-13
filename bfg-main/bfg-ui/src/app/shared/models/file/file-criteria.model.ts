export interface FileCriteriaData {
  fileStatus: FileStatus[];
  types: Type[];
  entity: Entity[];
  service: string[];
  direction: string[];
  bpstate: string[];
}

interface Entity {
  entityName: string;
  entityId: number;
  service: string;
}

interface FileStatus {
  service: string;
  outbound: boolean;
  label: string;
  title: string;
  status: number;
}

export interface Type {
  service: string;
  type: string;
}
