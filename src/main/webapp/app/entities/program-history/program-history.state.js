(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('program-history', {
            parent: 'entity',
            url: '/program-history?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProgramHistories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/program-history/program-histories.html',
                    controller: 'ProgramHistoryController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('program-history-detail', {
            parent: 'program-history',
            url: '/program-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProgramHistory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/program-history/program-history-detail.html',
                    controller: 'ProgramHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ProgramHistory', function($stateParams, ProgramHistory) {
                    return ProgramHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'program-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('program-history-detail.edit', {
            parent: 'program-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program-history/program-history-dialog.html',
                    controller: 'ProgramHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProgramHistory', function(ProgramHistory) {
                            return ProgramHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('program-history.new', {
            parent: 'program-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program-history/program-history-dialog.html',
                    controller: 'ProgramHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                rating: null,
                                feeling: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('program-history', null, { reload: 'program-history' });
                }, function() {
                    $state.go('program-history');
                });
            }]
        })
        .state('program-history.edit', {
            parent: 'program-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program-history/program-history-dialog.html',
                    controller: 'ProgramHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProgramHistory', function(ProgramHistory) {
                            return ProgramHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('program-history', null, { reload: 'program-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('program-history.delete', {
            parent: 'program-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program-history/program-history-delete-dialog.html',
                    controller: 'ProgramHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProgramHistory', function(ProgramHistory) {
                            return ProgramHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('program-history', null, { reload: 'program-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
