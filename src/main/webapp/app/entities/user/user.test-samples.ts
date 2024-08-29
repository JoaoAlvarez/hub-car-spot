import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: '14b69a4b-b808-4b85-9e07-0d4219800f1f',
  login: 'z5Q0kN',
};

export const sampleWithPartialData: IUser = {
  id: '84f5e165-3a3c-40cc-a6ee-9d56c22d1791',
  login: 'RS@v4\\}XkD\\awWo5A',
};

export const sampleWithFullData: IUser = {
  id: '42d97ed6-3672-4ecc-b3bb-550dfc6350dc',
  login: 'l}SB@F66',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
